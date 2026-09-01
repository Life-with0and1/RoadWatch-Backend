package com.example.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.post.model.DashboardStats;

public interface DashboardStatsRepository extends JpaRepository<DashboardStats, Long> {
}