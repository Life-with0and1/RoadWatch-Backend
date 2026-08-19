package com.example.post.projection;

import java.time.LocalDateTime;

import com.example.post.model.VoteType;

public interface VoteScoringProjection {
    VoteType getVoteType();
    LocalDateTime getCreatedAt();
    Double getLatitude();
    Double getLongitude();
}
