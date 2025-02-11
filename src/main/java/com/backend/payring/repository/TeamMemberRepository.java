package com.backend.payring.repository;

import com.backend.payring.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity,  Long> {
    List<TeamMemberEntity> findAllByRoomId(Long roomId);
}
