package com.backend.payring.exception;

import com.backend.payring.code.ErrorCode;

public class RoomException extends RuntimeException  {
  private final ErrorCode errorCode;

  public RoomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}