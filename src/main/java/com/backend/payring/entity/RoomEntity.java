package com.backend.payring.entity;

import com.backend.payring.entity.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoomEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "room_status")
    private RoomStatus roomStatus;


    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(nullable = false, name = "room_image")
    private String roomImage;

    @OneToMany(mappedBy = "room",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMemberEntity> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "room",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();
}
