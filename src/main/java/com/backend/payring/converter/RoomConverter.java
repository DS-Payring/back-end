package com.backend.payring.converter;

import com.backend.payring.dto.room.DurationStatusDTO;
import com.backend.payring.dto.room.RoomDTO;
import com.backend.payring.dto.room.TeamMemberDTO;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.TeamMemberEntity;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.entity.enums.RoomStatus;

import java.util.List;
import java.util.stream.Collectors;

public class RoomConverter {

    public static RoomEntity toEntity(UserEntity owner, RoomDTO.CreateReq req) {
        RoomEntity room = RoomEntity.builder()
                .roomName(req.getRoomName())
                .roomImage(req.getRoomImage())
                .roomStatus(RoomStatus.COLLECTING)
                .settleAmount(0)
                .totalAmount(0)
                .build();

        TeamMemberEntity teamMember = TeamMemberEntity.builder()
                .user(owner)
                .room(room)
                .isOwner(true)
                .isJoin(true)
                .build();

        room.getTeamMembers().add(teamMember);
        return room;
    }

    public static RoomDTO.DetailRes toRes(RoomEntity room) {
        List<TeamMemberDTO.Res> teamMembers = room.getTeamMembers().stream()
                .map(member -> TeamMemberDTO.Res.builder()
                        .userId(member.getUser().getId())
                        .userName(member.getUser().getUserName())
                        .email(member.getUser().getEmail())
                        .profileImage(member.getUser().getProfileImage())
                        .isOwner(member.getIsOwner())
                        .isJoin(member.getIsJoin())
                        .build())
                .collect(Collectors.toList());

        return RoomDTO.DetailRes.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .roomStatus(room.getRoomStatus())
                .roomImage(room.getRoomImage())
                .settleAmount(room.getSettleAmount())
                .totalAmount(room.getTotalAmount())
                .teamMembers(teamMembers)
                .build();
    }

    public static RoomDTO.InvitedRoomList toInvitedRoomRes(TeamMemberEntity teamMember) {
        RoomEntity room = teamMember.getRoom();
        String ownerName = room.getTeamMembers().stream()
                .filter(TeamMemberEntity::getIsOwner)
                .map(member -> member.getUser().getUserName())
                .findFirst()
                .orElse("");

        return RoomDTO.InvitedRoomList.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .roomImage(room.getRoomImage())
                .invitedAt(room.getCreatedAt())
                .build();
    }

    public static DurationStatusDTO.RoomInfo toRoomInfo(RoomEntity room) {
        return DurationStatusDTO.RoomInfo.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .totalAmount(room.getTotalAmount())
                .roomStatus(room.getRoomStatus().name()) // Enum -> String 변환
                .roomImage(room.getRoomImage())
                .build();
    }

    public static DurationStatusDTO.DurationStatus toDurationStatus(int totalSettledAmount, int unSettledAmount, List<DurationStatusDTO.RoomInfo> roomInfoList) {
        return DurationStatusDTO.DurationStatus.builder()
                .totalSettledAmount(totalSettledAmount)
                .unSettledAmount(unSettledAmount)
                .roomInfos(roomInfoList)
                .build();
    }
}
