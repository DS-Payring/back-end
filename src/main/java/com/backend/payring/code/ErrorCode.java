package com.backend.payring.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),



    /**
     * 401 UNAUTHORIZED: 토큰 만료
     */
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "요청 헤더에 토큰이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),

    /**
     * 403
     */


    /**
     * 404
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.OK, "정산 요청을 찾을 수 없습니다."),

    /**
     * 406
     * **/


    /**
     * 502
     */

    /**
     * temp
     */
    TEMP_EXCEPTION(HttpStatus.UNAUTHORIZED, "TEMP 생성에 실패했습니다."),

    UNABLE_TO_RESOLVE_HOST(HttpStatus.BAD_GATEWAY, "호스트를 찾을 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}