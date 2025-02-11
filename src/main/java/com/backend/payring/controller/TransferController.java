package com.backend.payring.controller;

import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.temp.TempCreateDTO;
import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/payments/{paymentId}/transfers")
public class TransferController {
    private final TransferService transferService;

    @Operation(
            summary = "송금하는 사람의 정보 조회 API",
            description = "송금하는 사람에게 송금하기 위한 상세 정보를 조회합니다."
    )
    @GetMapping("/receiver-info")
    public ResponseEntity<ResponseDTO<?>> getReceiverInfo(@PathVariable("paymentId") Long paymentId) {

        ReceiverDTO.TransferInfo res = transferService.getReceiverInfo(paymentId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_TEMP.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_TEMP, res));
    }
}
