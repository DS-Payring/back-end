package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.dto.response.ErrorResponseDTO;

public class GlobalException extends RuntimeException{
    private ErrorCode code;

    public GlobalException(ErrorCode code) {
        this.code = code;
    }

    public ErrorResponseDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
