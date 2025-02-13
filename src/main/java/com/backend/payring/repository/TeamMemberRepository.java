package com.backend.payring.repository;

import com.backend.payring.entity.TeamMemberEntity;
import com.backend.payring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity,  Long> {
    List<TeamMemberEntity> findAllByRoomId(Long roomId);
    List<TeamMemberEntity> findByUserIdAndIsJoinFalse(Long userId);

    @Query("SELECT tm.room.id FROM TeamMemberEntity tm WHERE tm.user.id = :userId")
    List<Long> findRoomIdsByUserId(@Param("userId") Long userId);
}
