package com.backend.payring.controller;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.payment.GetPaymentDTO;
import com.backend.payring.dto.payment.PaymentCreateDTO;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.transfer.CompletedUserDTO;
import com.backend.payring.dto.transfer.UnCompletedUserDTO;
import com.backend.payring.dto.transfer.UserTransferStatusDTO;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.exception.UserException;
import com.backend.payring.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "사용 내역 등록하기 API | 은서",
            description = "사용 내역을 등록하여 정산에 추가합니다."
    )
    @PostMapping(value = "/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> createPayment (
            @Valid @RequestPart("req") PaymentCreateDTO.Req req,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserEntity user
            ) {
        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        PaymentCreateDTO.Res res = paymentService.createPayment(req, image, user);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_PAYMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_PAYMENT, res));
    }

    @Operation(
            summary = "방별 정산 금액 요청 조회 API | 은서",
            description = "방별 정산 금액 요청을 모두 조회합니다."
    )
    @GetMapping("/{roomId}/payments")
    public ResponseEntity<ResponseDTO<?>> getPaymentList (
            @PathVariable("roomId") Long roomId,
            @AuthenticationPrincipal UserEntity user
    ) {

        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        GetPaymentDTO.PaymentList res = paymentService.getPaymentList(roomId, user);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PAYMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PAYMENT, res));
    }

    @Operation(
            summary = "방별 정산 금액 요청 조회 API | 은서",
            description = "방별 정산 금액 요청을 모두 조회합니다."
    )
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<ResponseDTO<?>> getPaymentDetail (@PathVariable("paymentId") Long paymentId) {
        GetPaymentDTO.PaymentDetail res = paymentService.getPaymentDetail(paymentId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PAYMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PAYMENT, res));
    }

    @Operation(
            summary = "정산 금액 삭제 API (정산 신청 전) | 은서",
            description = "정산을 신청하기 전에 등록한 정산을 삭제합니다."
    )
    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<ResponseDTO<?>> deletePayment (@PathVariable("paymentId") Long paymentId) {
        paymentService.deletePayment(paymentId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_PAYMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_PAYMENT, null));
    }

    @Operation(
            summary = "방 정산 요청하기 API | 은서",
            description = "방 정산을 요청합니다. 백엔드 내부에서 유저별로 송금해야 하는 금액을 계산하여 관리를 시작합니다."
    )
    @PostMapping("/{roomId}/payments/start")
    public ResponseEntity<ResponseDTO<?>> startSettling(@PathVariable("roomId") Long roomId) {

        paymentService.startSettling(roomId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DIVIDE_PAYMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DIVIDE_PAYMENT, null));
    }


    @Operation(
            summary = "정산 완료한 팀원 리스트 조회 API | 은서",
            description = "방 내에서 송금할 내역이 없거나, 송금을 완료한 유저를 조회합니다."
    )
    @GetMapping("/{roomId}/payments/finish")
    public ResponseEntity<ResponseDTO<?>> getFinishedTeamMemberList(@PathVariable("roomId") Long roomId) {

        List<CompletedUserDTO.UserInfo> res = paymentService.getFinishTeamMemberList(roomId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_USER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_USER, res));
    }

    @Operation(
            summary = "아직 정산 중인 팀원 리스트 조회 API | 은서",
            description = "송금 상태가 완료되지 않은 유저를 조회합니다."
    )
    @GetMapping("/{roomId}/payments/in-progress")
    public ResponseEntity<ResponseDTO<?>> getUnFinishedTeamMemberList(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable("roomId") Long roomId
    ) {

        List<UnCompletedUserDTO.SenderInfo> res = paymentService.getUnFinishedTeamMemberList(roomId, user.getId());

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_USER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_USER, res));
    }

    @Operation(
            summary = "현재 유저의 남은 정산 현황 조회 API | 은서",
            description = "보내야 하는 내역, 나한테 보내지 않은 사람(방 정산 페이지)을 조회합니다."
    )
    @GetMapping("/{roomId}/payments/status")
    public ResponseEntity<ResponseDTO<?>> getUserLeftSettlements(
            @PathVariable("roomId") Long roomId,
            @AuthenticationPrincipal UserEntity user
    ) {

        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        UserTransferStatusDTO.UserStatus res = paymentService.getUserTransferStatus(roomId, user);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_TRANSFER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_TRANSFER, res));
    }
}
