package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;

public class TempException extends GlobalException {
    public TempException(ErrorCode errorCode) {
        super(errorCode);
    }
}
