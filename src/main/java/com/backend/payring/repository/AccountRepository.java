package com.backend.payring.repository;

import com.backend.payring.entity.AccountEntity;
import com.backend.payring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findAllByUser(UserEntity user);
}
