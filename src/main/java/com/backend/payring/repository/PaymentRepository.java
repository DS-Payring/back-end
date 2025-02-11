package com.backend.payring.repository;

import com.backend.payring.entity.PaymentEntity;
import com.backend.payring.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findAllByRoomOrderByIdDesc(RoomEntity room);

    List<PaymentEntity> findAllByRoom(RoomEntity room);

    ;
}
