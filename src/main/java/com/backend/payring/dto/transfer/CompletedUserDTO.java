package com.backend.payring.dto.transfer;

import lombok.*;

import java.util.List;

public class CompletedUserDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserInfo {
        private Long id;
        private String userName;
        private String profileImage;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserInfoList {
        private List<UserInfo> completedUsers;
    }
}
