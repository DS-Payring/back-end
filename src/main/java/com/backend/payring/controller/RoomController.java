package com.backend.payring.controller;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.room.DurationStatusDTO;
import com.backend.payring.dto.room.RoomDTO;
import com.backend.payring.dto.room.TeamMemberDTO;
import com.backend.payring.dto.transfer.UserTransferStatusDTO;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.exception.UserException;
import com.backend.payring.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    // getDurationStatus
    private final RoomService roomService;

    @Operation(
            summary = "방 생성 API",
            description = "새로운 방을 생성합니다."
    )
    @PostMapping
    public ResponseEntity<ResponseDTO<RoomDTO.DetailRes>> createRoom(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody RoomDTO.CreateReq req
    ) {
        RoomDTO.DetailRes res = roomService.createRoom(user.getId(), req);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_ROOM.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_ROOM, res));
    }

    @Operation(
            summary = "방 삭제 API",
            description = "방 삭제는 방장만 가능하며, 정산 시작 전에만 가능합니다."
    )
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ResponseDTO<Void>> deleteRoom(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long roomId
    ) {
        roomService.deleteRoom(user.getId(), roomId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_ROOM.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_ROOM));
    }

    @Operation(
            summary = "방 목록 조회 API",
            description = "참여 중인 모든 방 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<ResponseDTO<List<RoomDTO.RoomList>>> getRoomList(
            @AuthenticationPrincipal UserEntity user
    ) {
        List<RoomDTO.RoomList> res = roomService.getRoomList(user.getId());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_ROOM_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_ROOM_LIST, res));
    }

    @Operation(
            summary = "방 상세 조회 API",
            description = "특정 방의 상세 정보를 조회합니다.."
    )
    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDTO<RoomDTO.DetailRes>> getRoomDetail(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long roomId
    ) {
        RoomDTO.DetailRes res = roomService.getRoomDetail(user.getId(), roomId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_ROOM.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_ROOM, res));
    }

    @Operation(
            summary = "방 멤버 목록 조회 API",
            description = "해당 방의 멤버 목록을 조회합니다."
    )
    @GetMapping("/{roomId}/members")
    public ResponseEntity<ResponseDTO<List<TeamMemberDTO.Res>>> getRoomMembers(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long roomId
    ) {
        List<TeamMemberDTO.Res> res = roomService.getRoomMembers(user.getId(), roomId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_ROOM_MEMBERS.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_ROOM_MEMBERS, res));
    }

    @Operation(
            summary = "멤버 초대 API",
            description = "새로운 멤버를 초대합니다."
    )
    @PostMapping("/invite")
    public ResponseEntity<ResponseDTO<Void>> inviteMember(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody TeamMemberDTO.InviteReq req
    ) {
        roomService.inviteMember(user.getId(), req);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_INVITE_MEMBER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_INVITE_MEMBER));
    }

    @Operation(
            summary = "초대 수락 API",
            description = "초대받은 멤버가 초대를 수락합니다."
    )
    @PostMapping("/{roomId}/accept")
    public ResponseEntity<ResponseDTO<Void>> acceptInvitation(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long roomId
    ) {
        roomService.acceptInvitation(user.getId(), roomId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_ACCEPT_INVITATION.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ACCEPT_INVITATION));
    }

    @Operation(
            summary = "초대 거절 API",
            description = "초대받은 멤버가 초대를 거절합니다."
    )
    @PostMapping("/{roomId}/reject")
    public ResponseEntity<ResponseDTO<Void>> rejectInvitation(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long roomId
    ) {
        roomService.rejectInvitation(user.getId(), roomId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_REJECT_INVITATION.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_REJECT_INVITATION));
    }

    @Operation(
            summary = "방 나가기 API",
            description = "멤버가 방을 나갑니다. 정산 시작 후에는 퇴장이 불가능합니다."
    )
    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<ResponseDTO<Void>> leaveRoom(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long roomId
    ) {
        roomService.leaveRoom(user.getId(), roomId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_LEAVE_ROOM.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_LEAVE_ROOM));
    }

    @Operation(
            summary = "초대된 방 목록 조회 API",
            description = "사용자가 초대받은 방 목록을 조회합니다."
    )
    @GetMapping("/invitations")
    public ResponseEntity<ResponseDTO<List<RoomDTO.InvitedRoomList>>> getInvitedRooms(
            @AuthenticationPrincipal UserEntity user
    ) {
        List<RoomDTO.InvitedRoomList> res = roomService.getInvitedRooms(user.getId());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_INVITED_ROOMS.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_INVITED_ROOMS, res));
    }
    
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
