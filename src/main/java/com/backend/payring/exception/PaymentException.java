package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;

public class PaymentException extends GlobalException {
  public PaymentException(ErrorCode errorCode) {
    super(errorCode);
  }
}