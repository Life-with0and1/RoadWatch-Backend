package com.example.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteSummaryDTO {
    
    private long resolved;
    private long stillHappening;
}
