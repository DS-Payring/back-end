package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;

public class GlobalException extends RuntimeException{
    private ErrorCode code;
    private String customMessage;

    public GlobalException(ErrorCode code) {
        this.code = code;
        this.customMessage = code.getMessage();
    }
}
