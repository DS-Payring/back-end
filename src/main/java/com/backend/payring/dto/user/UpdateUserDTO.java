package com.backend.payring.dto.user;

import lombok.*;

public class UpdateUserDTO {

    @Getter
    public static class Req {
        private String userName;
        private String payUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private Long userId;
        private String userName;
        private String email;
        private String profileImage;
        private String payUrl;
    }


}
