package com.example.instagramapi.service;

import com.example.instagramapi.dto.request.CommentCreateRequest;
import com.example.instagramapi.dto.response.CommentResponse;
import com.example.instagramapi.entity.Comment;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.CommentRepository;
import com.example.instagramapi.repository.PostRepository;
import com.example.instagramapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse create(
        Long postId,
        Long userId,
        CommentCreateRequest request
    ) {
        Post foundPost = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User foundUser = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(foundUser)
                .post(foundPost)
                .build();

        commentRepository.save(comment);
        return CommentResponse.from(comment);
    }



}
