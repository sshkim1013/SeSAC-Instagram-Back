package com.example.instagramapi.service;

import com.example.instagramapi.dto.request.PostCreateRequest;
import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.CommentRepository;
import com.example.instagramapi.repository.PostLikeRepository;
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
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

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
    public List<PostResponse> findAll(Long currentUserId) {
        List<Post> posts = postRepository.findAllWithUser();

        return posts.stream()
                .map(post -> toPostResponseWithStats(post, currentUserId))
                .toList();
    }

    // 단일 게시물
    public PostResponse findById(Long postId, Long currentUserId) {
        Post foundPost = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

//        return PostResponse.from(foundPost);
        return toPostResponseWithStats(foundPost, currentUserId);
    }


    // 특정 사용자 게시물
    public List<PostResponse> findByUsername(String username, Long currentUserId) {
        User foundUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Post> posts = postRepository.findByUserIdWithUser(foundUser.getId());

        return posts.stream()
                .map(post -> toPostResponseWithStats(post, currentUserId))
                .toList();
    }

    @Transactional
    public void delete(Long postId, Long userId) {
        Post foundPost = postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 해당 게시글의 작성자가 맞는지 검증
        if (!foundPost.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_POST_OWNER);
        }

        postRepository.delete(foundPost);
    }

    private PostResponse toPostResponseWithStats(Post post, Long currentUserId) {
        boolean liked = currentUserId != null
                && postLikeRepository.existsByUserIdAndPostId(currentUserId, post.getId());
        long likeCount = postLikeRepository.countByPostId(post.getId());
        long commentCount = commentRepository.countByPostId(post.getId());

        return PostResponse.from(post, liked, likeCount, commentCount);
    }

}
