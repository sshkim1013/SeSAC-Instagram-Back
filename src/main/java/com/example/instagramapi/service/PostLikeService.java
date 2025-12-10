package com.example.instagramapi.service;

import com.example.instagramapi.dto.response.LikeResponse;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.entity.PostLike;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.PostLikeRepository;
import com.example.instagramapi.repository.PostRepository;
import com.example.instagramapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponse like(Long userId, Long postId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post foundPost = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 이미 좋아요 했는지 확인하는 코드
        if (postLikeRepository.existsByUserIdAndPostId(foundUser.getId(), foundPost.getId())) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        PostLike postLike = PostLike.builder()
            .user(foundUser)
            .post(foundPost)
            .build();

        postLikeRepository.save(postLike);

        // 좋아요 갯수 확인하는 코드
        long likeCount = postLikeRepository.countByPostId(foundPost.getId());

        return LikeResponse.of(true, likeCount);
    }

}
