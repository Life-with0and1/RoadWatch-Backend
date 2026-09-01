package com.example.post.service;

import org.springframework.stereotype.Service;

import com.example.post.model.DashboardStats;
import com.example.post.repository.DashboardStatsRepository;

@Service
public class DashboardStatsService {

    private final DashboardStatsRepository dashboardStatsRepository;

    public DashboardStatsService(DashboardStatsRepository dashboardStatsRepository) {
        this.dashboardStatsRepository = dashboardStatsRepository;
    }

    public void incrementTotalPosts() {

        DashboardStats stats = dashboardStatsRepository.findById(1L)
                .orElseGet(() -> {
                    DashboardStats newStats = new DashboardStats();
                    newStats.setId(1L);
                    newStats.setTotalPosts(0);
                    newStats.setResolvedPosts(0);
                    return newStats;
                });

        stats.setTotalPosts(stats.getTotalPosts() + 1);

        dashboardStatsRepository.save(stats);
    }

    public void incrementResolvedPosts() {

        DashboardStats stats = dashboardStatsRepository.findById(1L)
                .orElseGet(() -> {
                    DashboardStats newStats = new DashboardStats();
                    newStats.setId(1L);
                    newStats.setResolvedPosts(0);
                    return newStats;
                });

        stats.setResolvedPosts(stats.getResolvedPosts() + 1);

        dashboardStatsRepository.save(stats);
    }

    public void addCity(String city) {

        DashboardStats stats = dashboardStatsRepository.findById(1L)
                .orElseGet(() -> {
                    DashboardStats newStats = new DashboardStats();
                    newStats.setId(1L);
                    newStats.setResolvedPosts(0);
                    return newStats;
                });

        stats.getCities().add(city);

        dashboardStatsRepository.save(stats);
    }
}