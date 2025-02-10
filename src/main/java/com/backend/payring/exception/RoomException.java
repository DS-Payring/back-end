package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;

public class RoomException extends GlobalException {
  public RoomException(ErrorCode errorCode) {
    super(errorCode);
  }
}
