package com.example.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user.model.PendingRegisterUser;

public interface PendingRegisterRepository extends JpaRepository<PendingRegisterUser, Long> {
}
