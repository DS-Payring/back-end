package com.backend.payring.dto.transfer;

import lombok.*;

import java.util.List;

public class ReceiveDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Receive {
        private Long transferId;
        private String transferImage;
        private String receiverName;
        private String receiverImage;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Receiver {
        // 내가 받지 않은 않은 송금 리스트
        List<UserTransferStatusDTO.NotReceived> notReceived;
        // 내가 받은 송금 리스트 조회
        List<Receive> receives;
    }

}
