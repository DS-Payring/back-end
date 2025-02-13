package com.backend.payring.converter;

import com.backend.payring.dto.payment.GetPaymentDTO;
import com.backend.payring.dto.payment.PaymentCreateDTO;
import com.backend.payring.entity.PaymentEntity;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentConverter {

    public static PaymentEntity toPaymentEntity(PaymentCreateDTO.Req req, UserEntity user, RoomEntity room, String url) {
        return PaymentEntity.builder()
                .amount(req.getAmount())
                .title(req.getTitle())
                .memo(req.getMemo())
                .user(user)
                .room(room)
                .paymentImage(url)
                .build();
    }

    public static PaymentCreateDTO.Res toRes(PaymentEntity payment) {
        return PaymentCreateDTO.Res.builder()
                .amount(payment.getAmount())
                .title(payment.getTitle())
                .memo(payment.getMemo())
                .paymentImage(payment.getPaymentImage())
                .build();
    }

    public static GetPaymentDTO.PaymentDetail toPaymentDetail(PaymentEntity payment) {
        return GetPaymentDTO.PaymentDetail.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .title(payment.getTitle())
                .memo(payment.getMemo())
                .paymentImage(payment.getPaymentImage())
                .isTransfer(payment.getIsTransfer())
                .roomId(payment.getRoom().getId()) // N + 1
                .userId(payment.getUser().getId())
                .build();
    }

    public static GetPaymentDTO.PaymentList toPaymentList(List<PaymentEntity> payments, Long currentUserId, Boolean isCollecting) {
        List<GetPaymentDTO.PaymentDetail> paymentDetails = payments.stream()
                .map(payment -> {
                    boolean isWriter = isCollecting && payment.getUser().getId().equals(currentUserId); // isCollecting이 false면 무조건 false

                    return GetPaymentDTO.PaymentDetail.builder()
                            .id(payment.getId())
                            .amount(payment.getAmount())
                            .title(payment.getTitle())
                            .memo(payment.getMemo())
                            .paymentImage(payment.getPaymentImage())
                            .isTransfer(payment.getIsTransfer())
                            .roomId(payment.getRoom().getId())
                            .userId(payment.getUser().getId())
                            .isWriter(isWriter) // isCollecting이 false면 무조건 false
                            .build();
                })
                .collect(Collectors.toList());

        // totalAmount 계산
        int totalAmount = payments.stream()
                .mapToInt(PaymentEntity::getAmount)
                .sum();

        return GetPaymentDTO.PaymentList.builder()
                .totalAmount(totalAmount)
                .payments(paymentDetails)
                .build();
    }


}
