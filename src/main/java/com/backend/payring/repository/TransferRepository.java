package com.backend.payring.repository;

import com.backend.payring.entity.TransferEntity;
import com.backend.payring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    @Query("SELECT DISTINCT t.sender.id FROM TransferEntity t " +
            "WHERE t.room.id = :roomId AND (t.isComplete = true OR t.sender IS NULL)")
    List<Long> findCompletedSenders(@Param("roomId") Long roomId);

    @Query("SELECT DISTINCT t.sender.id FROM TransferEntity t WHERE t.room.id = :roomId")
    List<Long> findDistinctSenderIds(@Param("roomId") Long roomId);

    @Query("SELECT t FROM TransferEntity t WHERE t.room.id = :roomId AND t.isComplete = false")
    List<TransferEntity> findUnCompletedTransfers(@Param("roomId") Long roomId);

    // 완료된 송금 금액 합산 (`isComplete=true`)
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransferEntity t WHERE t.room.id = :roomId AND t.isComplete = true")
    int sumCompletedTransfersByRoomId(@Param("roomId") Long roomId);

    // 미완료된 송금 금액 합산 (`isComplete=false`)
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransferEntity t WHERE t.room.id = :roomId AND t.isComplete = false")
    int sumUncompletedTransfersByRoomId(@Param("roomId") Long roomId);

    List<TransferEntity> findByRoomIdAndSenderAndIsCompleteFalse(Long roomId, UserEntity user);

    List<TransferEntity> findByRoomIdAndSenderAndIsCompleteTrue(Long roomId, UserEntity user);

    List<TransferEntity> findByRoomIdAndReceiverAndIsCompleteFalse(Long roomId, UserEntity user);

    List<TransferEntity> findByRoomIdAndReceiverAndIsCompleteTrue(Long roomId, UserEntity user);
}
