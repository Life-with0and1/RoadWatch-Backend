package com.example.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.post.model.PostVote;
import com.example.post.projection.VoteScoringProjection;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote,Long> {   
    Optional<PostVote> findByPostIdAndUserId(Long postId, Long userId);

    @Query("""
    SELECT v.voteType AS voteType,
           v.createdAt AS createdAt,
           v.latitude AS latitude,
           v.longitude AS longitude
        FROM PostVote v
        WHERE v.post.id = :postId
        """)
    List<VoteScoringProjection> findVoteForScoring(@Param("postId") Long postId);

}