package com.backend.payring.repository;

import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findByIdInAndCreatedAtAfterAndRoomStatusNot(List<Long> roomIds, LocalDateTime createdAt, RoomStatus roomStatus);
}
