package com.backend.payring.dto.room;

import lombok.*;
import org.checkerframework.checker.units.qual.N;

public class TeamMemberDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InviteReq {
        private Long roomId;
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Res {
        private Long teamMemberId;
        private Long userId;
        private String userName;
        private String email;
        private String profileImage;
        private Boolean isOwner;
        private Boolean isJoin;
    }
}
