package com.backend.payring.dto.transfer;

import lombok.*;

import java.util.List;

public class UnCompletedUserDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SenderInfo {
        private Long userId;
        private String userName;
        private String profileImage;
        private Integer totalLeftAmount;
        private List<ReceiverInfo> receiverInfos;

        public void updateTotalLeftAmount(Integer totalLeftAmount) {
            this.totalLeftAmount = totalLeftAmount;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReceiverInfo {
        private Long receiverId;
        private String receiverName;
        private Integer amount;
        private Boolean isSenderForMe;
    }
}
