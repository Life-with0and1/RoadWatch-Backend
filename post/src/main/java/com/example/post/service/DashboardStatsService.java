package com.example.post.service;

import org.springframework.stereotype.Service;

import com.example.post.dto.DashboardStatsResponseDTO;
import com.example.post.model.DashboardStats;
import com.example.post.repository.DashboardStatsRepository;

import jakarta.transaction.Transactional;

@Service
public class DashboardStatsService {

    private final DashboardStatsRepository dashboardStatsRepository;

    public DashboardStatsService(DashboardStatsRepository dashboardStatsRepository) {
        this.dashboardStatsRepository = dashboardStatsRepository;
    }

    @Transactional
    public void incrementTotalPosts() {
        DashboardStats stats = getOrCreateStats();

        stats.setTotalPosts(stats.getTotalPosts() + 1);

        dashboardStatsRepository.save(stats);
    }

    @Transactional
    public void incrementResolvedPosts() {
        DashboardStats stats = getOrCreateStats();

        stats.setResolvedPosts(stats.getResolvedPosts() + 1);

        dashboardStatsRepository.save(stats);
    }

    @Transactional
    public void addCity(String city) {
        DashboardStats stats = getOrCreateStats();

        stats.getCities().add(city);

        dashboardStatsRepository.save(stats);
    }

    @Transactional
    public DashboardStatsResponseDTO getStats() {
        DashboardStats stats = getOrCreateStats();

        return new DashboardStatsResponseDTO(
                stats.getTotalPosts(),
                stats.getResolvedPosts(),
                stats.getCities().size()
        );
    }

    private DashboardStats getOrCreateStats() {
        return dashboardStatsRepository.findById(1L)
                .orElseGet(() -> {
                    DashboardStats newStats = new DashboardStats();
                    newStats.setId(1L);
                    newStats.setTotalPosts(0);
                    newStats.setResolvedPosts(0);
                    return dashboardStatsRepository.save(newStats);
                });
    }
}