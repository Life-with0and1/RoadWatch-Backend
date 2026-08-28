package com.example.user.scheduler;

import com.example.user.repository.PendingRegisterRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PendingUserCleanupScheduler {

    private final PendingRegisterRepository pendingRegisterRepository;

    public PendingUserCleanupScheduler(PendingRegisterRepository pendingRegisterRepository) {
        this.pendingRegisterRepository = pendingRegisterRepository;
    }

    @Scheduled(fixedRate = 600000) 
    public void cleanupExpiredUsers() {
        pendingRegisterRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }
}