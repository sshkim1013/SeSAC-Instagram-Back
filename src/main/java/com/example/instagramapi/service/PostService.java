package com.example.instagramapi.service;

import com.example.instagramapi.dto.request.PostCreateRequest;
import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.PostRepository;
import com.example.instagramapi.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse create(Long userId, PostCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }


    // 전체 게시물
    public List<PostResponse> findAll() {
        List<Post> posts = postRepository.findAllWithUser();

        return posts.stream()
                .map(PostResponse::from)
                .toList();
    }

    // 단일 게시물
    public PostResponse findById(Long postId) {
        Post foundPost = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return PostResponse.from(foundPost);
    }


    // 특정 사용자 게시물
    public List<PostResponse> findByUsername(String username) {
        User foundUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Post> posts = postRepository.findByUserIdWithUser(foundUser.getId());

        return posts.stream()
                .map(PostResponse::from)
                .toList();
    }

}
