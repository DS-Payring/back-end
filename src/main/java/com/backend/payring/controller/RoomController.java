package com.backend.payring.controller;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.room.DurationStatusDTO;
import com.backend.payring.dto.transfer.UserTransferStatusDTO;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.exception.UserException;
import com.backend.payring.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    // getDurationStatus
    private final RoomService roomService;

    @Operation(
            summary = "기간별 방별 정산 현황 조회 API",
            description = "기간 내에 송금해야 하는 금액, 송금해야 하는 금액을 조회합니다."
    )
    @GetMapping("/status")
    public ResponseEntity<ResponseDTO<?>> getUnFinishedTeamMemberList(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam(value = "period", defaultValue = "7") Integer day
    ) {

        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        DurationStatusDTO.DurationStatus res = roomService.getDurationStatus(day, user);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_ROOM_STATISTICS.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_ROOM_STATISTICS, res));
    }
}
