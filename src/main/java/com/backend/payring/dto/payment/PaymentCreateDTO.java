package com.backend.payring.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class PaymentCreateDTO {

    @Getter
    public static class Req {
        // projectId
        @NotNull(message = "projectId는 필수 입력 값입니다.")
        private Long projectId;
        // 금액
        @NotNull(message = "amount는 필수 입력 값입니다.")
        private Integer amount;
        // 제목
        @NotBlank(message = "title은 필수 입력 값입니다.")
        private String title;
        // 메모
        private String memo;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        // 금액
        private Integer amount;
        // 제목
        private String title;
        // 메모
        private String memo;
        // 정산 이미지
        private String paymentImage;
    }

}
