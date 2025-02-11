package com.backend.payring.controller;

import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.payment.GetPaymentDTO;
import com.backend.payring.dto.payment.PaymentCreateDTO;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.temp.TempCreateDTO;
import com.backend.payring.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "사용 내역 등록하기 API",
            description = "사용 내역을 등록하여 정산에 추가합니다."
    )
    @PostMapping(value = "/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> createPayment (
            @Valid @RequestPart("req") PaymentCreateDTO.Req req,
            @RequestPart(value = "image", required = false) MultipartFile image
            ) {

        PaymentCreateDTO.Res res = paymentService.createPayment(req, image);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_PAYMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_PAYMENT, res));
    }

    @Operation(
            summary = "방별 정산 금액 요청 조회 API (정산 신청 전)",
            description = "방별 정산 금액 요청을 모두 조회합니다."
    )
    @GetMapping("/{roomId}/payments")
    public ResponseEntity<ResponseDTO<?>> getPaymentList (@PathVariable("roomId") Long roomId) {

        GetPaymentDTO.PaymentList res = paymentService.getPaymentList(roomId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PAYMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PAYMENT, res));
    }

    @Operation(
            summary = "방별 정산 금액 요청 조회 API (정산 신청 전)",
            description = "방별 정산 금액 요청을 모두 조회합니다."
    )
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<ResponseDTO<?>> getPaymentDetail (@PathVariable("paymentId") Long paymentId) {
        System.out.println(paymentId);
        GetPaymentDTO.PaymentDetail res = paymentService.getPaymentDetail(paymentId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PAYMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PAYMENT, res));
    }
}
