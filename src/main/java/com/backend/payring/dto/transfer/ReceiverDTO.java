package com.backend.payring.dto.transfer;

import lombok.*;

import java.util.List;

public class ReceiverDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TransferInfo {
        // 방 아이디
        private Long roomId;
        // 방 이름
        private String roomName;
        // 유저 아이디
        private Long userId;
        // 유저 이름
        private String username;
        // 카카오 링크
        private String payUrl;
        // 유저 계좌
        private List<Account> accounts;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Account {
        // 계좌 id
        private Long accountId;
        // 계좌 이름
        private String bankName;
        // 계좌 번호
        private String accountNo;
        // 계좌 수신자
        private String receiver;
    }

}
