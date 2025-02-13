package com.backend.payring.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

public class UserDTO {
    @Getter
    @Builder
    public static class SignUpReq {
        @NotBlank(message = "이름을 입력해 주세요.")
        private String userName;

        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해 주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        private String password;

        private String profileImage;
        private String payUrl;
        private List<AccountReq> accounts;

        @AssertTrue(message = "계좌 또는 카카오페이 링크 중 하나를 반드시 입력해 주세요.")
        private boolean isValidPaymentInfo() {
            return (accounts != null && !accounts.isEmpty()) || (payUrl != null && !payUrl.trim().isEmpty());
        }
    }

    @Getter
    @Builder
    public static class AccountReq {
        @NotBlank(message = "은행명을 입력해 주세요.")
        private String bankName;

        @NotBlank(message = "계좌번호를 입력해 주세요.")
        private String accountNo;

        @NotBlank(message = "예금주를 입력해 주세요.")
        private String receiver;
    }

    @Getter
    @Builder
    public static class LoginReq {
        @NotBlank(message = "이메일을 입력해 주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private Long userId;
        private String name;
        private String email;
        private String profileImage;
        private String payUrl;
        private List<AccountRes> accounts;
        private String token;
        @Setter
        @Getter
        private String message;

        public Res(String message) {
            this.message = message;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AccountRes {
        private Long accountId;
        private String bankName;
        private String accountNo;
        private String receiver;
    }

    @Getter
    @Builder
    public static class EmailVerificationReq {
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해 주세요.")
        private String email;

        @NotBlank(message = "인증번호를 입력해 주세요.")
        private String verificationCode;
    }

}
