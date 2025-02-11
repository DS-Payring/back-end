package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.TransferConverter;
import com.backend.payring.dto.transfer.*;
import com.backend.payring.entity.*;
import com.backend.payring.exception.TransferException;
import com.backend.payring.repository.AccountRepository;
import com.backend.payring.repository.PaymentRepository;
import com.backend.payring.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService{

    private final TransferRepository transferRepository;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final S3Uploader s3Uploader;

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
    public VerifyTransferDTO.Res verifyTransfer(Long transferId, MultipartFile image) {
        // 이미지가 없으면 예외 처리
        if (image == null) {
            throw new TransferException(ErrorCode.IMAGE_REQUIRED);
        }

        TransferEntity transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new TransferException(ErrorCode.TRANSFER_NOT_FOUND));

        if (transfer.getIsComplete()) {
            throw new TransferException(ErrorCode.ALREADY_COMPLETED_TRANSFER);
        }

        String url = "transfer url";
// 인증 구현되면 s3 업로드 로직 추가하기
//
//        try {
//            url = s3Uploader.upload(image, "payment");
//            log.info("S3 업로드 성공: {}", url);
//        } catch (IOException e) {
//            log.error("S3 업로드 실패: {}", image.getOriginalFilename(), e);
//            throw new RuntimeException("S3 업로드 실패", e);
//        }

        transfer.verify(url);
        transferRepository.save(transfer);

        return TransferConverter.toVerifyRes(transfer);
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
