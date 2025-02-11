package com.backend.payring.controller;

import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.transfer.ReceiveDTO;
import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.dto.transfer.SendDTO;
import com.backend.payring.dto.transfer.VerifyTransferDTO;
import com.backend.payring.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class TransferController {
    private final TransferService transferService;

    @Operation(
            summary = "송금하는 사람의 정보 조회 API",
            description = "송금하는 사람에게 송금하기 위한 상세 정보를 조회합니다."
    )
    @GetMapping("/transfers/{transferId}/receiver-info")
    public ResponseEntity<ResponseDTO<?>> getReceiverInfo(@PathVariable("transferId") Long transferId) {

        ReceiverDTO.TransferInfo res = transferService.getReceiverInfo(transferId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_USER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_USER, res));
    }

    @Operation(
            summary = "송금 인증 API (인증 기능 미구현)",
            description = "이미지를 업로드하여 송금을 인증합니다. 수취인, 금액이 일치하지 않으면 송금이 인증되지 않습니다."
    )
    @PostMapping(value = "/transfers/{transferId}/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> verifyTransfer(
            @PathVariable("transferId") Long transferId,
            @RequestPart(value = "image") MultipartFile image
    ) {
        VerifyTransferDTO.Res res = transferService.verifyTransfer(transferId, image);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_VERIFY_TRANSFER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_VERIFY_TRANSFER, res));
    }

    @Operation(
            summary = "내가 보낸 송금 리스트 조회 + 내가 보내지 않은 송금 리스트 조회 API",
            description = "내가 받은 송금 리스트와 내가 받지 않은 송금 리스트를 조회합니다."
    )
    @GetMapping("/{roomId}/transfers/send/{userId}") // userId 지우기
    public ResponseEntity<ResponseDTO<?>> getSenderTransferStatus(
            @PathVariable("roomId") Long roomId,
            @PathVariable("userId") Long userId
    ) {
        SendDTO.Sender res = transferService.getSenderTransferStatus(roomId, userId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_TRANSFER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_TRANSFER, res));
    }

    @Operation(
            summary = "내가 받은 송금 리스트 조회 + 내가 받지 않은 송금 리스트 조회 API",
            description = "내가 받은 송금 리스트와 내가 받지 않은 송금 리스트를 조회합니다."
    )
    @GetMapping("/{roomId}/transfers/receive/{userId}") // userId 지우기
    public ResponseEntity<ResponseDTO<?>> getReceiverTransferStatus(
            @PathVariable("roomId") Long roomId,
            @PathVariable("userId") Long userId
    ) {
        ReceiveDTO.Receiver res = transferService.getReceiverTransferStatus(roomId, userId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_TRANSFER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_TRANSFER, res));
    }
}
