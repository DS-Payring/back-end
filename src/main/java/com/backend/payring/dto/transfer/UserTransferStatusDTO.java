package com.backend.payring.dto.transfer;

import lombok.*;

import java.util.List;

public class UserTransferStatusDTO {
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NotReceived {
        private Long transferId;
        private String sender;
        private Integer amount;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NotSent {
        private Long transferId;
        private String receiver;
        private Integer amount;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserStatus {
        // 내가 받지 않음
        private List<NotReceived> notReceived;
        // 내가 보내지 않음
        private List<NotSent> notSent;
    }
}
