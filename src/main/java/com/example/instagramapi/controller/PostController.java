package com.example.instagramapi.controller;

import com.example.instagramapi.dto.request.CommentCreateRequest;
import com.example.instagramapi.dto.request.PostCreateRequest;
import com.example.instagramapi.dto.response.ApiResponse;
import com.example.instagramapi.dto.response.CommentResponse;
import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.security.CustomUserDetails;
import com.example.instagramapi.service.CommentService;
import com.example.instagramapi.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody PostCreateRequest request
    ) {
        PostResponse response = postService.create(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> findAll() {
        List<PostResponse> posts = postService.findAll();
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> findById(@PathVariable Long id) {
        PostResponse response = postService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.delete(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody CommentCreateRequest request
    ) {
        CommentResponse response = commentService.create(postId, userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }



}
