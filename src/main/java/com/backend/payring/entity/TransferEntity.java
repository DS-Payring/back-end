package com.backend.payring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TransferEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    @Column(nullable = false, name = "amount")
    private Integer amount;

    @Column(name = "transfer_image")
    private String transferImage;

    @Column(name = "is_complete")
    private Boolean isComplete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "room_id")
    private RoomEntity room;

    // 발송인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "sender_id")
    private UserEntity sender;

    // 수취인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "receiver_id")
    private UserEntity receiver;

    // 송금 인증
    public void verify(String imageUrl) {
        this.transferImage = imageUrl;
        this.isComplete = true;
    }
}
