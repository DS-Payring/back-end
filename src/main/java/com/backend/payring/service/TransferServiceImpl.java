package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.TransferConverter;
import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.dto.transfer.VerifyTransferDTO;
import com.backend.payring.entity.*;
import com.backend.payring.exception.PaymentException;
import com.backend.payring.exception.TransferException;
import com.backend.payring.repository.AccountRepository;
import com.backend.payring.repository.PaymentRepository;
import com.backend.payring.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
}
