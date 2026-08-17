package com.example.post.dto;

import com.example.post.model.VoteType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddVoteDTO {
    @NotNull
    private VoteType voteType;
}
