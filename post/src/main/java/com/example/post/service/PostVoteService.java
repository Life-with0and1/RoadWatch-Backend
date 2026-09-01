package com.example.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.post.dto.AddVoteDTO;
import com.example.post.exception.BadRequestException;
import com.example.post.exception.PostNotFoundException;
import com.example.post.model.Post;
import com.example.post.model.PostVote;
import com.example.post.model.VoteType;
import com.example.post.projection.VoteScoringProjection;
import com.example.post.repository.PostRepository;
import com.example.post.repository.PostVoteRepository;

import jakarta.transaction.Transactional;

@Service
public class PostVoteService {
    
    private final PostRepository postRepository;
    private final DashboardStatsService dashboardStatsService; 
    private final PostVoteRepository postVoteRepository;
    private final GeoLocationService geoLocationService;
    private final PostService postService;

    public PostVoteService(PostRepository postRepository, DashboardStatsService dashboardStatsService, PostVoteRepository postVoteRepository, GeoLocationService geoLocationService, PostService postService){
        this.postRepository = postRepository;
        this.dashboardStatsService = dashboardStatsService;
        this.postVoteRepository = postVoteRepository;
        this.geoLocationService = geoLocationService;
        this.postService = postService;
    }

    @Transactional
    public void addVote(Long postId, Long userId, AddVoteDTO dto) {
        Post post = postRepository.findById(postId).orElseThrow(() ->  new PostNotFoundException("Post not found with id: " + postId));

        double distance = geoLocationService.calculateDistance(post.getLatitude(),post.getLongitude(),dto.getLatitude(),dto.getLongitude());
        if (distance > 1.0) {
            throw new BadRequestException("You must be within 1 km of the post location to vote");
        }

        PostVote vote = postVoteRepository.findByPostIdAndUserId(postId,userId).orElseGet(() -> new PostVote());
        vote.setPost(post);
        vote.setUserId(userId);
        vote.setLatitude(dto.getLatitude());
        vote.setLongitude(dto.getLongitude());
        vote.setVoteType(dto.getVoteType());

        postVoteRepository.save(vote);
        evaluatePostResolution(postId);
    }

    private void evaluatePostResolution(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId) );
        List<VoteScoringProjection> votes = postVoteRepository.findVoteForScoring(postId);
        if (votes.size() < 3) {
            return;
        }

        double resolvedScore = 0.0;
        double stillHappeningScore = 0.0;

        for (VoteScoringProjection vote : votes) {

            double distance = geoLocationService.calculateDistance(post.getLatitude(),post.getLongitude(),vote.getLatitude(),vote.getLongitude());

            double distanceWeight = geoLocationService.calculateDistanceWeight(distance);

            double recencyWeight = geoLocationService.calculateRecencyWeight(vote.getCreatedAt());

            double finalWeight = distanceWeight * recencyWeight;

            if (vote.getVoteType() == VoteType.RESOLVED) {
                resolvedScore += finalWeight;
            } else if (vote.getVoteType() == VoteType.STILL_HAPPENING) {
                stillHappeningScore += finalWeight;
            }
        }
        double totalScore = resolvedScore + stillHappeningScore;
        if (totalScore >= 3.0) {
            double resolvedRatio = resolvedScore / totalScore;
            if (resolvedRatio >= 0.75) {
                postService.deletePostWithMedia(post);
                dashboardStatsService.incrementResolvedPosts();
            }
        }
    }
}
