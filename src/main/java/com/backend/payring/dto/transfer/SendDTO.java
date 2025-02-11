package com.backend.payring.dto.transfer;

import lombok.*;

import java.util.List;

public class SendDTO {


    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Sent {
        private Long transferId;
        private String transferImage;
        private String receiverName;
        private String receiverImage;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Sender {
        // 내가 보내지 않은 송금 리스트
        List<UserTransferStatusDTO.NotSent> notSents;
        // 내가 보낸 송금 리스트 조회
        List<Sent> sents;
    }

}
