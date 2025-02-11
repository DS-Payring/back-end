package com.backend.payring.repository;

import com.backend.payring.dto.transfer.CompleteUserDTO;
import com.backend.payring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByIdIn(List<Long> userIds);
}
