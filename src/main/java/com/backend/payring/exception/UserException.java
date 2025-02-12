package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
  private final ErrorCode errorCode;

  public UserException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
