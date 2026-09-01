package com.example.post.service;


import com.example.post.dto.CreatePostDTO;
import com.example.post.dto.PostMediaResponseDTO;
import com.example.post.dto.PostResponseDTO;
import com.example.post.dto.UpdatePostDTO;
import com.example.post.dto.VoteSummaryDTO;
import com.example.post.exception.BadRequestException;
import com.example.post.exception.ForbiddenException;
import com.example.post.exception.PostCreationException;
import com.example.post.exception.PostNotFoundException;
import com.example.post.model.MediaType;
import com.example.post.model.Post;
import com.example.post.model.PostMedia;
import com.example.post.model.VoteType;
import com.example.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final DashboardStatsService dashboardStatsService;
    private final CloudinaryService cloudinaryService;
    private final MediaValidationService mediaValidationService;
    private final RateLimiterService rateLimiterService;

    public PostService(PostRepository postRepository, DashboardStatsService dashboardStatsService, CloudinaryService cloudinaryService, MediaValidationService mediaValidationService, RateLimiterService rateLimiterService){
        this.postRepository = postRepository;
        this.dashboardStatsService = dashboardStatsService;
        this.cloudinaryService = cloudinaryService;
        this.mediaValidationService = mediaValidationService;
        this.rateLimiterService = rateLimiterService;
    }

    public PostResponseDTO createPost(CreatePostDTO postDTO,long userId, List<MultipartFile> mediaFiles) {
        if (!rateLimiterService.tryConsume(userId)) {
            throw new BadRequestException("Please wait before creating another post.");
        }
        mediaValidationService.validate(mediaFiles);
        Post post = new Post();

        post.setDescription(postDTO.getDescription());
        post.setCategory(postDTO.getCategory());
        post.setCity(postDTO.getCity());
        post.setLatitude(postDTO.getLatitude());
        post.setLongitude(postDTO.getLongitude());
        post.setUserId(userId);

        Set<String> uploadedPublicIds = new HashSet<>();
        try {
            for (MultipartFile file : mediaFiles) {

                UploadResult uploadResult = cloudinaryService.uploadFile(file);

                String mediaUrl = uploadResult.getUrl();
                String publicId = uploadResult.getPublicId();
                uploadedPublicIds.add(publicId);

                PostMedia postMedia = new PostMedia();
                postMedia.setMediaUrl(mediaUrl);
                postMedia.setPublicId(publicId);
                postMedia.setPost(post);

                if (file.getContentType() != null && file.getContentType().startsWith("video")) {
                    postMedia.setMediaType(MediaType.VIDEO);
                } else {
                    postMedia.setMediaType(MediaType.IMAGE);
                }

                post.getMedia().add(postMedia);
            }
            dashboardStatsService.addCity(postDTO.getCity());
            return toResponseDTO(postRepository.save(post));
        }
        catch (Exception e) {
                for (String publicId : uploadedPublicIds) {
                    try {
                        cloudinaryService.deleteFile(publicId);
                    } catch (IOException deleteException) {
                        throw new PostCreationException("Unable to delete post media", e);
                    }
                }
                throw new PostCreationException("Unable to create post. Please try again.",e);
            }
    }


    public Page<PostResponseDTO> fetchNearbyPosts(double latitude, double longitude, double radius, Pageable pageable) {
        if (radius <= 0 || radius > 5) {
            throw new BadRequestException("Radius must be under 5 kilometers");
        }
        return postRepository.findNearbyPosts(latitude, longitude, radius, pageable).map(this::toResponseDTO);
    }

    private PostResponseDTO toResponseDTO(Post post) {

        List<PostMediaResponseDTO> media = post.getMedia()
                .stream()
                .map(postMedia -> new PostMediaResponseDTO(
                    postMedia.getMediaUrl(),
                    postMedia.getPublicId(),
                    postMedia.getMediaType()
                ))
                .toList();

        long resolved = post.getVotes()
            .stream()
            .filter(vote -> vote.getVoteType() == VoteType.RESOLVED)
            .count();

        long stillHappening = post.getVotes()
            .stream()
            .filter(vote -> vote.getVoteType() == VoteType.STILL_HAPPENING)
            .count();

        VoteSummaryDTO summary = new VoteSummaryDTO(resolved, stillHappening);

        return new PostResponseDTO(
                post.getId(),
                post.getUserId(),
                post.getLatitude(),
                post.getLongitude(),
                post.getDescription(),
                summary,
                post.getCategory(),
                post.getCreatedAt(),
                media
        );
    }

    public PostResponseDTO fetchPostById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        return toResponseDTO(post);
    }

    public void deletePost(long postId, long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException( "Post not found with id: " + postId));
        if (post.getUserId() != userId) {
            throw new ForbiddenException("You are not allowed to delete this post");
        }
        deletePostWithMedia(post);
    }

    public void deletePostWithMedia(Post post) {
        for (PostMedia media : post.getMedia()) {
            try {
                cloudinaryService.deleteFile(media.getPublicId());
            } catch (IOException e) {
                throw new PostCreationException("Unable to delete post media", e);
            }
        }
        postRepository.delete(post);
    }

    public PostResponseDTO updatePost(UpdatePostDTO dto, Long postId, Long userId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        if(post.getUserId() != userId) {
            throw new ForbiddenException("You are not allowed to update this post");
        }
        
        post.setCategory(dto.getCategory());
        post.setDescription(dto.getDescription());

        Post savedPost = postRepository.save(post);

        return toResponseDTO(savedPost);
    }

    public Long getTotalPosts() {
        return postRepository.count();
    }
}