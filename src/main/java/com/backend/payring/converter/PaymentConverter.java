package com.backend.payring.converter;

import com.backend.payring.dto.payment.PaymentCreateDTO;
import com.backend.payring.entity.PaymentEntity;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.UserEntity;

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
}
