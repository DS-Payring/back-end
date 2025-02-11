package com.backend.payring.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {
    /**
     * User
     */
    SUCCESS_RETRIEVE_USER(HttpStatus.OK, "유저 조회를 성공했습니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다."),
    SUCCESS_RETRIEVE_USERINFO(HttpStatus.OK,"유저 정보를 성공적으로 가져왔습니다."),
    SUCCESS_UPDATE_APPLICANT(HttpStatus.OK, "유저 정보를 성공적으로 수정했습니다."),

    /**
     * payment
     */
    SUCCESS_CREATE_PAYMENT(HttpStatus.CREATED, "payment를 성공적으로 생성했습니다."),
    SUCCESS_RETRIEVE_PAYMENT(HttpStatus.OK, "payment를 성공적으로 조회했습니다."),
    SUCCESS_DELETE_PAYMENT(HttpStatus.OK, "payment를 성공적으로 삭제했습니다."),
    SUCCESS_DIVIDE_PAYMENT(HttpStatus.OK, "payment을 성공적으로 분배했습니다."),
    /**
     * Temp
     */
    SUCCESS_CREATE_TEMP(HttpStatus.OK, "temp를 성공적으로 생성했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}