package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.TransferConverter;
import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.entity.AccountEntity;
import com.backend.payring.entity.PaymentEntity;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.exception.PaymentException;
import com.backend.payring.repository.AccountRepository;
import com.backend.payring.repository.PaymentRepository;
import com.backend.payring.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService{

    private final TransferRepository transferRepository;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    @Override
    public ReceiverDTO.TransferInfo getReceiverInfo(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(ErrorCode.PAYMENT_NOT_FOUND));

        RoomEntity room = payment.getRoom();
        UserEntity user = payment.getUser();

        List<AccountEntity> accounts = accountRepository.findAllByUser(user);

        return TransferConverter.toTransferInfo(room, user, accounts);
    }
}
