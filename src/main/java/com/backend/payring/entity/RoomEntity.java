package com.backend.payring.entity;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.entity.enums.RoomStatus;
import com.backend.payring.exception.RoomException;
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
    private String roomName;

    // 정산 모으는 중 or 정산 중 or 정산 마감
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "room_status")
    private RoomStatus roomStatus;

    // 방 정산 시작일
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    // 방 정산 종료일
    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(nullable = false, name = "room_image")
    private String roomImage;

    // 정산이 필요한 금액 (정산 후, 송금 후 수정됨)
    @Column(name = "settle_amount")
    private Integer settleAmount;

    // 총 정산해야 하는 금액 (수정되지 않음)
    @Column(name = "total_amount")
    private Integer totalAmount;

    @OneToMany(mappedBy = "room",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferEntity> transfers = new ArrayList<>();

    @OneToMany(mappedBy = "room",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMemberEntity> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "room",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    // 정산 빼기
    public void subtractSettleAmount(Integer subtractAmount) {
        if (this.settleAmount < subtractAmount) {
            throw new RoomException(ErrorCode.INVALID_AMOUNT);
        }
        this.settleAmount -= subtractAmount;
    }

    // 총 정산할 금액 업데이트
    public void updateSettleAmount(Integer settleAmount) {
        this.settleAmount = settleAmount;
        this.totalAmount = settleAmount;
        this.roomStatus = RoomStatus.SETTLING;
    }

    // 현재 상태 변경
    public void finishSettlement() {
        this.roomStatus = RoomStatus.SETTLED;
    }
}
