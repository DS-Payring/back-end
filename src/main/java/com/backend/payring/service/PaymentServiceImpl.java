package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.PaymentConverter;
import com.backend.payring.converter.TransferConverter;
import com.backend.payring.converter.UserConverter;
import com.backend.payring.dto.payment.GetPaymentDTO;
import com.backend.payring.dto.payment.PaymentCreateDTO;
import com.backend.payring.dto.transfer.CompletedUserDTO;
import com.backend.payring.dto.transfer.UnCompletedUserDTO;
import com.backend.payring.dto.transfer.UserTransferStatusDTO;
import com.backend.payring.entity.*;
import com.backend.payring.entity.enums.RoomStatus;
import com.backend.payring.exception.PaymentException;
import com.backend.payring.exception.RoomException;
import com.backend.payring.exception.UserException;
import com.backend.payring.repository.*;
import com.backend.payring.service.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final S3Uploader s3Uploader;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final TransferRepository transferRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public PaymentCreateDTO.Res createPayment(PaymentCreateDTO.Req req, MultipartFile image, UserEntity user) {
        String url = null;

        // 이미지를 업로드했다면 s3에 업로드
        if (image != null) {
            try {
                url = s3Uploader.upload(image, "payment");
                log.info("S3 업로드 성공: {}", url);
            } catch (IOException e) {
                log.error("S3 업로드 실패: {}", image.getOriginalFilename(), e);
                throw new RuntimeException("S3 업로드 실패", e);
            }
        } else {
            log.info("업로드할 파일이 제공되지 않았습니다.");
        }

        RoomEntity room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        if (!room.getRoomStatus().equals(RoomStatus.COLLECTING)) {
            throw new RoomException(ErrorCode.NOT_COLLECTING);
        }

        PaymentEntity payment = PaymentConverter.toPaymentEntity(req, user, room, url);

        paymentRepository.save(payment);
        return PaymentConverter.toRes(payment);

    }

    @Override
    @Transactional(readOnly = true)
    public GetPaymentDTO.PaymentList getPaymentList(Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        List<PaymentEntity> payments = paymentRepository.findAllByRoomOrderByIdDesc(room);

        return PaymentConverter.toPaymentList(payments);
    }

    @Override
    @Transactional(readOnly = true)
    public GetPaymentDTO.PaymentDetail getPaymentDetail(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(ErrorCode.PAYMENT_NOT_FOUND));

        return PaymentConverter.toPaymentDetail(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(ErrorCode.PAYMENT_NOT_FOUND));

        paymentRepository.delete(payment);
    }

    @Override
    @Transactional
    public void startSettling(Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        // 해당 방의 모든 결제 내역 조회
        List<PaymentEntity> payments = paymentRepository.findAllByRoom(room);

        // 정산을 모으는 방이 아닐 경우 예외 처리
        if (!room.getRoomStatus().equals(RoomStatus.COLLECTING)) {
            throw new RoomException(ErrorCode.NOT_COLLECTING);
        }

        // 정산할 금액이 없다면 예외 처리
        if (payments.isEmpty()) {
            throw new RoomException(ErrorCode.NO_PAYMENT);
        }

        // 유저별 부담한 금액 & 받아야 하는 금액 계산
        Map<UserEntity, Integer> userPaidMap = new HashMap<>();  // 유저별 부담한 총 금액
        Map<UserEntity, Integer> userShouldPayMap = new HashMap<>(); // 유저별 받아야 할 금액

        for (PaymentEntity payment : payments) {
            UserEntity payer = payment.getUser(); // 돈을 낸 사람
            Integer amount = payment.getAmount(); // 지출한 금액

            // 유저별 총 지출 금액 계산 (ex 김은서, 40000) -> 김은서가 총 지출한 금액 40000
            userPaidMap.put(payer, userPaidMap.getOrDefault(payer, 0) + amount);

            // 개인별 지출해야 하는 금액 계산
            List<TeamMemberEntity> teamMembers = room.getTeamMembers();
            int perPersonAmount = amount / teamMembers.size();

            for (TeamMemberEntity member : teamMembers) {
                UserEntity receiver = member.getUser(); // 정산 대상
                userShouldPayMap.put(receiver, userShouldPayMap.getOrDefault(receiver, 0) + perPersonAmount); // 정산 대상에 추가함
            }
        }

        // 유저별 차액을 계산해서 송금해야 할 정보 생성
        Map<UserEntity, Integer> userBalanceMap = new HashMap<>();

        for (UserEntity user : userShouldPayMap.keySet()) {
            int paidAmount = userPaidMap.getOrDefault(user, 0); // 내가 지출한 금액
            int shouldPay = userShouldPayMap.getOrDefault(user, 0); // 내가 내야 할 금액
            int balance = shouldPay - paidAmount; // 양수면 더 내야 함, 음수면 받아야 함 (내가 더 많이 지출한 경우)

            userBalanceMap.put(user, balance);
        }

        // 송금 정보 저장 리스트
        List<TransferEntity> transfers = new ArrayList<>();
        int totalSettleAmount = 0;

        for (UserEntity sender : userBalanceMap.keySet()) {
            int senderBalance = userBalanceMap.get(sender);

            // 양수 -> 돈을 더 보내야 하는 상황
            if (senderBalance > 0) {

                // 돈을 받아야 하는 사람 for문 돌림
                for (UserEntity receiver : userBalanceMap.keySet()) {
                    int receiverBalance = userBalanceMap.get(receiver);

                    // 음수 -> 돈을 더 받아야 하는 상황
                    if (receiverBalance < 0) {

                        // 여기서 만약 다른 사람이 받아야 할 돈보다 내가 보내야 할 돈이 많은데 송금을 해버리면 다른 사람은 원래 받아야 하는 양보다 더 받아버림
                        // 내가 보낼 돈이 다른 사람이 받아야 할 돈보다 작아야 송금을 보내야 송금을 최소화할 수 있음. 따라서 min 메서드를 사용함
                        int transferAmount = Math.min(senderBalance, -receiverBalance);

                        TransferEntity transfer = TransferConverter.toTransfer(room, sender, receiver, transferAmount);

                        transfers.add(transfer);
                        totalSettleAmount += transferAmount;

                        senderBalance -= transferAmount;
                        userBalanceMap.put(sender, senderBalance);
                        receiverBalance += transferAmount;
                        userBalanceMap.put(receiver, receiverBalance);
                    }
                }
            }
        }


        transferRepository.saveAll(transfers);
        // 정산해야 하는 금액 설정
        room.updateSettleAmount(totalSettleAmount);
        roomRepository.save(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompletedUserDTO.UserInfo> getFinishTeamMemberList(Long roomId) {
        List<TeamMemberEntity> teamMembers = teamMemberRepository.findAllByRoomId(roomId);

        // 팀원의 userId 조회
        List<Long> teamMemberIds = teamMembers.stream()
                .map(member -> member.getUser().getId())
                .toList();

        // 정산 완료된 sender
        List<Long> settledSenderIds = transferRepository.findCompletedSenders(roomId);

        // 모든 sender
        List<Long> allSenders = transferRepository.findDistinctSenderIds(roomId);

        // 송금한 적 없는 유저
        List<Long> nonSenders = teamMemberIds.stream()
                .filter(userId -> !allSenders.contains(userId))
                .toList();

        // 송금 완료된 유저 + 송금할 필요 없는 유저
        List<Long> finalUserIds = new ArrayList<>();
        finalUserIds.addAll(settledSenderIds);
        finalUserIds.addAll(nonSenders);

        List<UserEntity> users = userRepository.findByIdIn(finalUserIds);

        return UserConverter.toUserInfoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnCompletedUserDTO.SenderInfo> getUnFinishedTeamMemberList(Long roomId) {
        // 아직 정산하지 않은 송금 내역
        List<TransferEntity> unCompletedTransfers = transferRepository.findUnCompletedTransfers(roomId);

        Map<Long, UnCompletedUserDTO.SenderInfo> senderInfoMap = new HashMap<>();

        for (TransferEntity transfer : unCompletedTransfers) {
            Long senderId = transfer.getSender().getId();
            Long receiverId = transfer.getReceiver().getId();
            int amount = transfer.getAmount();

            if (!senderInfoMap.containsKey(senderId)) {
                UserEntity sender = transfer.getSender();
                senderInfoMap.put(senderId, TransferConverter.toSenderInfo(sender));
            }

            // 송금해야 할 대상 추가
            UnCompletedUserDTO.SenderInfo senderInfo = senderInfoMap.get(senderId);
            senderInfo.getReceiverInfos().add(TransferConverter.toReceiverInfo(receiverId, transfer, amount));

            // 총 송금해야 하는 금액
            senderInfo.updateTotalLeftAmount(senderInfo.getTotalLeftAmount() + amount);
        }

        return new ArrayList<>(senderInfoMap.values());
    }

    @Override
    @Transactional(readOnly = true)
    public UserTransferStatusDTO.UserStatus getUserTransferStatus(Long roomId, UserEntity user) {
        // isComplete == false & sender == userId & roomId
        List<TransferEntity> notSentTransfers = transferRepository.findByRoomIdAndSenderAndIsCompleteFalse(roomId, user);

        // isComplete == false & receiver == user & roomId
        List<TransferEntity> notReceivedTransfers = transferRepository.findByRoomIdAndReceiverAndIsCompleteFalse(roomId, user);

        // 아직 보내지 않은 정산 리스트
        List<UserTransferStatusDTO.NotSent> notSentList = notSentTransfers.stream()
                .map(TransferConverter::toNotSent)
                .collect(Collectors.toList());

        // 아직 받지 못한 정산 리스트
        List<UserTransferStatusDTO.NotReceived> notReceivedList = notReceivedTransfers.stream()
                .map(TransferConverter::toNotReceived)
                .collect(Collectors.toList());

        return TransferConverter.toUserStatus(notSentList, notReceivedList);
    }




}
