package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;

public class TransferException extends GlobalException {
    public TransferException(ErrorCode errorCode) {
        super(errorCode);
    }
}
