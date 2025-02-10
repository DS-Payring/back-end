package com.backend.payring.dto.temp;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class TempCreateDTO {
    @Getter
    public static class Req {
        @NotBlank(message = "requestMessage는 필수 값입니다.")
        private String requestMessage;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private String responseMessage;
    }
}
