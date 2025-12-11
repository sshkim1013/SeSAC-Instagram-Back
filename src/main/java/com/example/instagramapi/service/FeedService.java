package com.example.instagramapi.service;

import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.dto.response.SliceResponse;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.repository.CommentRepository;
import com.example.instagramapi.repository.FollowRepository;
import com.example.instagramapi.repository.PostLikeRepository;
import com.example.instagramapi.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    private PostResponse toPostResponseWithStats(Post post, Long currentUserId) {
        boolean liked = currentUserId != null
            && postLikeRepository.existsByUserIdAndPostId(currentUserId, post.getId());
        long likeCount = postLikeRepository.countByPostId(post.getId());
        long commentCount = commentRepository.countByPostId(post.getId());

        return PostResponse.from(post, liked, likeCount, commentCount);
    }

    // 피드 -> 내가 팔로우하는 살마들의 게시물 + 내 게시물
    public SliceResponse<PostResponse> getFeed(Long userId, Pageable pageable) {
        // 팔로잉하는 사람 목록
        List<Long> followingIds = followRepository.findFollowingIdByFollowerId(userId);

        // 팔로잉 + 본인
        followingIds.add(userId);

        // 게시물 조회(페이징)
        Slice<Post> posts = postRepository.findByUserIdsWithUserPaging(followingIds, pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(post -> toPostResponseWithStats(post, userId))
                .toList();

        return SliceResponse.from(posts, content);
    }

}
