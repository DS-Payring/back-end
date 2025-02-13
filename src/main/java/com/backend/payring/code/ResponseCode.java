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
    SUCCESS_SIGNUP(HttpStatus.OK, "회원가입을 성공했습니다."),
    SUCCESS_RETRIEVE_USERINFO(HttpStatus.OK,"유저 정보를 성공적으로 가져왔습니다."),
    SUCCESS_UPDATE_USERINFO(HttpStatus.OK, "유저 정보를 성공적으로 수정했습니다."),
    SUCCESS_SEND_EMAIL(HttpStatus.OK, "이메일을 성공적으로 전송했습니다."),
    SUCCESS_VERIFY_EMAIL(HttpStatus.OK, "이메일을 성공적으로 인증했습니다."),

    /**
     * room
     */
    SUCCESS_RETRIEVE_ROOM_STATISTICS(HttpStatus.OK, "기간별 방 정보를 성공적으로 조회했습니다."),

    /**
     * payment
     */
    SUCCESS_CREATE_PAYMENT(HttpStatus.CREATED, "payment를 성공적으로 생성했습니다."),
    SUCCESS_RETRIEVE_PAYMENT(HttpStatus.OK, "payment를 성공적으로 조회했습니다."),
    SUCCESS_DELETE_PAYMENT(HttpStatus.OK, "payment를 성공적으로 삭제했습니다."),
    SUCCESS_DIVIDE_PAYMENT(HttpStatus.OK, "payment을 성공적으로 분배했습니다."),

    /**
     * transfer
     */
    SUCCESS_VERIFY_TRANSFER(HttpStatus.OK, "송금을 성공적으로 인증했습니다."),
    SUCCESS_RETRIEVE_TRANSFER(HttpStatus.OK, "송금 내역을 성공적으로 조회했습니다."),
    SUCCESS_SENDING_REMIND(HttpStatus.OK, "독촉 메일을 성공적으로 보냈습니다."),
    /**
     * Temp
     */
    SUCCESS_CREATE_TEMP(HttpStatus.OK, "temp를 성공적으로 생성했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}