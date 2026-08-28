package com.example.user.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.user.model.PendingRegisterUser;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;

public interface PendingRegisterRepository extends JpaRepository<PendingRegisterUser, Long> {
    public void deleteAllByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM PendingRegisterUser p WHERE p.expirationTime < :now")
    void deleteByExpirationTimeBefore(@Param("now") LocalDateTime now);
}
