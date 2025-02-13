package com.backend.payring.dto.payment;

import com.backend.payring.entity.RoomEntity;
import lombok.*;

import java.util.List;

public class GetPaymentDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PaymentDetail {
        private Long id;
        private Integer amount;
        private String title;
        private String memo;
        private String paymentImage;
        private Long roomId;
        private Long userId;
        private Boolean isWriter;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PaymentList {
        private Integer totalAmount;
        private List<PaymentDetail> payments;
    }
}
