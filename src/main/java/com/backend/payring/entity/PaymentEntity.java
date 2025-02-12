package com.backend.payring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false, name = "amount")
    private Integer amount;

    @Column(nullable = false, name = "title")
    private String title;

    private String memo;

    @Column(name = "payment_image")
    private String paymentImage;

    @Column(name = "is_transfer")
    private Boolean isTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "room_id")
    private RoomEntity room;

    // 돈을 지불한 사람 (정산을 받아야 하는 사람)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    public void updatePaymentImage (String url) {
        this.paymentImage = url;
    }
}
