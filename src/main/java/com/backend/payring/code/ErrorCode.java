package com.backend.payring.code;

import com.backend.payring.dto.response.ErrorResponseDTO;
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
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "남은 정산 금액보다 큰 금액을 송금할 수 없습니다."),
    NO_PAYMENT(HttpStatus.BAD_REQUEST,  "정산할 금액이 없어 정산을 시작할 수 없습니다."),
    NOT_COLLECTING(HttpStatus.BAD_REQUEST, "이미 정산이 시작되었거나 종료된 방입니다."),
    IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "송금 인증을 위해 이미지를 업로드해야 합니다."),
    ALREADY_COMPLETED_TRANSFER(HttpStatus.BAD_REQUEST, "이미 승인된 송금 요청입니다."),

    /**
     * 401
     */
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "요청 헤더에 토큰이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    TRANSFER_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "정보가 일치하지 않아 송금 인증에 실패했습니다."),
    RECEIVER_NOT_MATCH(HttpStatus.UNAUTHORIZED, "수취인의 정보가 일치하지 않아 송금 인증에 실패했습니다."),
    AMOUNT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "송금 금액이 인식되지 않아 송금 인증에 실패했습니다."),
    AMOUNT_NOT_MATCH(HttpStatus.UNAUTHORIZED, "송금 금액이 일치하지 않아 송금 인증에 실패했습니다."),

    /**
     * 403
     */
    NOT_SENDER(HttpStatus.UNAUTHORIZED, "송금 대상자가 아닙니다."),


    /**
     * 404
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "정산 요청을 찾을 수 없습니다."),
    TRANSFER_NOT_FOUND(HttpStatus.NOT_FOUND, "정산 내역을 찾을 수 없습니다."),

    /**
     * 406
     * **/

    /**
     * 500
     */
    VISION_API_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Google Cloud Vision API 호출 실패"),

    /**
     * 502
     */

    /**
     * temp
     */
    TEMP_EXCEPTION(HttpStatus.UNAUTHORIZED, "TEMP 생성에 실패했습니다."),

    UNABLE_TO_RESOLVE_HOST(HttpStatus.BAD_GATEWAY, "호스트를 찾을 수 없습니다."),


    /**
     * 회원 가입 관련
     */
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),

    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다."),

    EMAIL_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "이메일 인증이 완료되지 않았습니다."),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.")

    ;

    private final HttpStatus status;
    private final String message;

    public ErrorResponseDTO getReasonHttpStatus() {
        return ErrorResponseDTO.builder()
                .message(message)
                .status(status.value())
                .isSuccess(false)
                .error(this.name())
                .build()
                ;
    }


}