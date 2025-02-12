package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends GlobalException {
  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }
}
