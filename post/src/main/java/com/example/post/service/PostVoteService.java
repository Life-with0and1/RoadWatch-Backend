package com.example.post.service;

import org.springframework.stereotype.Service;

import com.example.post.dto.AddVoteDTO;
import com.example.post.exception.BadRequestException;
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

        double distance = calculateDistance(post.getLatitude(),post.getLongitude(),dto.getLatitude(),dto.getLongitude());

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
    }

    private double calculateDistance(double lat1,double lon1,double lat2,double lon2) {
    double earthRadius = 6371.0;

    double lat1Rad = Math.toRadians(lat1);
    double lat2Rad = Math.toRadians(lat2);

    double lonDifferenceRad = Math.toRadians(lon2 - lon1);

    double angle = Math.acos(
                    Math.sin(lat1Rad) * Math.sin(lat2Rad)
                    + Math.cos(lat1Rad)
                    * Math.cos(lat2Rad)
                    * Math.cos(lonDifferenceRad)
                );
    return earthRadius * angle;
}



}
