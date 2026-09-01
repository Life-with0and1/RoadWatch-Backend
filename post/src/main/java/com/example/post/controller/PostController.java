package com.example.post.controller;

import com.example.post.dto.CreatePostDTO;
import com.example.post.dto.PostResponseDTO;
import com.example.post.dto.UpdatePostDTO;
import com.example.post.security.CustomUserPrincipal;
import com.example.post.service.PostService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> createPost(@AuthenticationPrincipal CustomUserPrincipal user, @Valid @RequestPart("post") CreatePostDTO dto, @RequestPart("media") List<MultipartFile> media) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(dto, user.getUserId(), media));
    }

    @GetMapping("/nearby")
    public ResponseEntity<Page<PostResponseDTO>> fetchNearbyPosts(@RequestParam double latitude,@RequestParam double longitude,@RequestParam(defaultValue = "5") double radius,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdAt"));

        return ResponseEntity.ok(postService.fetchNearbyPosts(latitude,longitude,radius,pageable)
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponseDTO> fetchPostById(@PathVariable long postId) {
        return ResponseEntity.ok(postService.fetchPostById(postId));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void>  deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserPrincipal user){
        postService.deletePost(postId, user.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/post/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserPrincipal user,@Valid UpdatePostDTO dto){
        return ResponseEntity.ok(postService.updatePost(dto, postId, user.getUserId()));
    }

    @GetMapping("/stats/posts")
    public ResponseEntity<Long> getTotalPosts() {
        Long totalPosts = postService.getTotalPosts();
        return ResponseEntity.ok(totalPosts);
    }
}
