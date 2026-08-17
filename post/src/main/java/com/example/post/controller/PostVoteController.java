package com.example.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.post.dto.AddVoteDTO;
import com.example.post.security.CustomUserPrincipal;
import com.example.post.service.PostVoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostVoteController {
    
    private final PostVoteService postVoteService;

    public PostVoteController(PostVoteService postVoteService){
        this.postVoteService = postVoteService;
    }

    @PostMapping("/{postId}/vote")
    public ResponseEntity<Void> addVote(@PathVariable Long postId,@AuthenticationPrincipal CustomUserPrincipal user , @Valid @RequestBody AddVoteDTO dto){
        postVoteService.addVote(postId, user.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }
}
