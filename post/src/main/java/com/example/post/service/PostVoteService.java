package com.example.post.service;

import org.springframework.stereotype.Service;

import com.example.post.dto.AddVoteDTO;
import com.example.post.exception.PostNotFoundException;
import com.example.post.model.Post;
import com.example.post.model.PostVote;
import com.example.post.repository.PostRepository;
import com.example.post.repository.PostVoteRepository;

@Service
public class PostVoteService {
    
    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;

    public PostVoteService(PostRepository postRepository, PostVoteRepository postVoteRepository){
        this.postRepository = postRepository;
        this.postVoteRepository = postVoteRepository;
    }

    public void addVote(Long postId, Long userId, AddVoteDTO dto) {
        Post post = postRepository.findById(postId).orElseThrow(() ->  new PostNotFoundException("Post not found with id: " + postId));

        PostVote vote = postVoteRepository.findByPostIdAndUserId(postId,userId).orElseGet(() -> new PostVote());
        vote.setPost(post);
        vote.setUserId(userId);
        vote.setVoteType(dto.getVoteType());

        postVoteRepository.save(vote);
    }



}
