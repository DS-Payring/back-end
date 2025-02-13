package com.backend.payring.dto.room;

import com.backend.payring.entity.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.util.List;

public class RoomDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReq {
        private String roomName;
        private String roomImage;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReq {
        private String roomName;
        private String roomImage;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailRes {
        private Long roomId;
        private String roomName;
        private RoomStatus roomStatus;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;
        private String roomImage;
        private Integer settleAmount;
        private Integer totalAmount;
        private List<TeamMemberDTO.Res> teamMembers;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvitedRoomList {
        private Long roomId;
        private String roomName;
        private String roomImage;
        private LocalDateTime invitedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomList {
        private Long roomId;
        private String roomName;
        private String roomImage;
        private RoomStatus roomStatus;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;
        private Integer totalAmount;
        private Integer memberCount;
        private Boolean isOwner;
    }
}
