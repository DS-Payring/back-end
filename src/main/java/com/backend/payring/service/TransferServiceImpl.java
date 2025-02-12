package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.TransferConverter;
import com.backend.payring.dto.transfer.*;
import com.backend.payring.entity.*;
import com.backend.payring.exception.TransferException;
import com.backend.payring.repository.AccountRepository;
import com.backend.payring.repository.PaymentRepository;
import com.backend.payring.repository.TransferRepository;
import com.backend.payring.service.ocr.GoogleCloudVisionService;
import com.backend.payring.service.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService{

    private final TransferRepository transferRepository;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final S3Uploader s3Uploader;
    private final GoogleCloudVisionService googleCloudVisionService;

    @Override
    public ReceiverDTO.TransferInfo getReceiverInfo(Long transferId) {
        TransferEntity transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new TransferException(ErrorCode.TRANSFER_NOT_FOUND));

        RoomEntity room = transfer.getRoom();
        UserEntity user = transfer.getReceiver();

        List<AccountEntity> accounts = accountRepository.findAllByUser(user);

        return TransferConverter.toTransferInfo(room, user, accounts);
    }

    @Override
    @Transactional
    public VerifyTransferDTO.Res verifyTransfer(Long transferId, Long userId, MultipartFile image) {
        // 이미지가 없으면 예외 처리
        if (image == null) {
            throw new TransferException(ErrorCode.IMAGE_REQUIRED);
        }

        TransferEntity transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new TransferException(ErrorCode.TRANSFER_NOT_FOUND));

        // sender와 일치하지 않으면 예외 처리
        if (!transfer.getSender().getId().equals(userId)) {
            throw new TransferException(ErrorCode.NOT_SENDER);
        }

        // 이미 완료된 송금이라면 예외 처리
        if (transfer.getIsComplete()) {
            throw new TransferException(ErrorCode.ALREADY_COMPLETED_TRANSFER);
        }

        String extractedText = null;
        try {
            // Google Cloud Vision API 호출하여 이미지에서 텍스트 추출
            extractedText = googleCloudVisionService.extractText(image);
            log.info("Vision API OCR 결과: {}", extractedText);

        } catch (IOException e) {
            log.error("Google Cloud Vision API 호출 실패", e);
            throw new TransferException(ErrorCode.VISION_API_FAILED);
        }

        // OCR 결과 검증
        if (!isValidTransferAmount(extractedText, transfer.getAmount(), transfer.getReceiver())) {
            throw new TransferException(ErrorCode.TRANSFER_VERIFICATION_FAILED);
        }

        String url;
        try {
            url = s3Uploader.upload(image, "transfer");
            log.info("S3 업로드 성공: {}", url);
        } catch (IOException e) {
            log.error("S3 업로드 실패: {}", image.getOriginalFilename(), e);
            throw new RuntimeException("S3 업로드 실패", e);
        }

        transfer.verify(url);
        transferRepository.save(transfer);

        //방 정산 필요 금액 감소
        RoomEntity room = transfer.getRoom();
        room.subtractSettleAmount(transfer.getAmount());

        if (room.getSettleAmount() == 0) {
            room.finishSettlement();
        }

        return TransferConverter.toVerifyRes(transfer);
    }

    private boolean isValidTransferAmount(String extractedText, Integer expectedAmount, UserEntity receiver) {
        if (extractedText == null || extractedText.isEmpty()) {
            return false;
        }

        String userName = receiver.getUserName();

        // 유저의 모든 계좌
        List<String> receiverNames = receiver.getAccounts().stream()
                .map(AccountEntity::getReceiver)
                .toList();

        // OCR 결과에서 "숫자+원" 패턴
        Pattern pattern = Pattern.compile("(\\d{1,3}(,\\d{3})*|\\d+)원");
        Matcher matcher = pattern.matcher(extractedText);

        List<Integer> amounts = new ArrayList<>();
        while (matcher.find()) {
            String numberString = matcher.group(1).replaceAll(",", ""); // 쉼표 제거
            amounts.add(Integer.parseInt(numberString));
        }

        // OCR 결과에서 유저 이름 또는 계좌 수신인(receiver)이 포함되어 있는지 확인
        boolean containsValidReceiver = extractedText.contains(userName) ||
                receiverNames.stream().anyMatch(extractedText::contains);

        if (!containsValidReceiver) {
            throw new TransferException(ErrorCode.RECEIVER_NOT_MATCH);
        }

        // OCR 결과에 금액이 포함되어 있는지 확인
        if (amounts.isEmpty()) {
            throw new TransferException(ErrorCode.AMOUNT_NOT_FOUND);
        }

        // OCR로 읽은 금액이 예상 금액과 일치하는지 확인
        if (!amounts.contains(expectedAmount)) {
            log.info("amounts : " + amounts.toString());
            log.info("expectedAmount : " + expectedAmount.toString());
            throw new TransferException(ErrorCode.AMOUNT_NOT_MATCH);
        }

        // 송금 성공
        return true;
    }






    @Override
    @Transactional(readOnly = true)
    public SendDTO.Sender getSenderTransferStatus(Long roomId, Long userId) {
        List<TransferEntity> notSentTransfers = transferRepository.findByRoomIdAndSenderIdAndIsCompleteFalse(roomId, userId);

        List<TransferEntity> sentTransfers = transferRepository.findByRoomIdAndSenderIdAndIsCompleteTrue(roomId, userId);

        // 아직 보내지 않은 송금 리스트
        List<UserTransferStatusDTO.NotSent> notSentList = notSentTransfers.stream()
                .map(TransferConverter::toNotSent)
                .collect(Collectors.toList());

        // 이미 보낸 송금 리스트
        List<SendDTO.Sent> sentList = sentTransfers.stream()
                .map(TransferConverter::toSent)
                .collect(Collectors.toList());

        return TransferConverter.toSender(notSentList, sentList);
    }

    @Override
    @Transactional(readOnly = true)
    public ReceiveDTO.Receiver getReceiverTransferStatus(Long roomId, Long userId) {
        List<TransferEntity> notReceivedTransfers = transferRepository.findByRoomIdAndReceiverIdAndIsCompleteFalse(roomId, userId);

        List<TransferEntity> receivedTransfers = transferRepository.findByRoomIdAndReceiverIdAndIsCompleteTrue(roomId, userId);

        List<UserTransferStatusDTO.NotReceived> notReceivedList = notReceivedTransfers.stream()
                .map(TransferConverter::toNotReceived)
                .collect(Collectors.toList());

        List<ReceiveDTO.Receive> receivedList = receivedTransfers.stream()
                .map(TransferConverter::toReceive)
                .collect(Collectors.toList());

        return TransferConverter.toReceiver(notReceivedList, receivedList);
    }


}
