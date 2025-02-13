package com.backend.payring.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamMemberEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, name = "is_owner")
    private Boolean isOwner;

    @Column(nullable = false, name = "is_join")
    private Boolean isJoin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "room_id")
    private RoomEntity room;

    public void acceptInvitation() {
        this.isJoin = true;
    }

    public void rejectInvitation() {
        this.isJoin = false;
    }
}
