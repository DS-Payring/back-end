package com.backend.payring.dto.transfer;

import lombok.*;

public class VerifyTransferDTO {
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private Integer amount;
        private String transferImage;
        private Boolean isComplete;
    }
}
