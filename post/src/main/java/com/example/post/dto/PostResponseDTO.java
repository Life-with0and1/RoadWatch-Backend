package com.example.post.dto;

import com.example.post.model.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

    private long id;
    private long userId;
    private Double latitude;
    private Double longitude;
    private String description;
    private VoteSummaryDTO voteSummary;
    private Category category;
    private LocalDateTime createdAt;
    private List<PostMediaResponseDTO> media;
}