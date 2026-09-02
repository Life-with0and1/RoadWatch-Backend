package com.example.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardStatsResponseDTO {

    private long totalPosts;
    private long resolvedPosts;
    private long totalCities;
}