package com.backend.payring.service;

import com.backend.payring.dto.room.DurationStatusDTO;
import com.backend.payring.dto.room.RoomDTO;
import com.backend.payring.dto.room.TeamMemberDTO;
import com.backend.payring.entity.UserEntity;

import java.util.List;

public interface RoomService {
    RoomDTO.DetailRes createRoom(Long userId, RoomDTO.CreateReq req);
    void deleteRoom(Long userId, Long roomId);
    void inviteMember(Long userId, TeamMemberDTO.InviteReq req);
    void acceptInvitation(Long userId, Long roomId);
    void rejectInvitation(Long userId, Long roomId);
    void leaveRoom(Long userId, Long roomId);
    List<RoomDTO.InvitedRoomList> getInvitedRooms(Long userId);
    DurationStatusDTO.DurationStatus getDurationStatus(Integer day, UserEntity user);
}