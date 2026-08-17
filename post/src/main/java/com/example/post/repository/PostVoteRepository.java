package com.example.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.post.model.PostVote;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote,Long> {   
    Optional<PostVote> findByPostIdAndUserId(Long postId, Long userId);
}