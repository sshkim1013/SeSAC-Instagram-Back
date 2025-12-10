package com.example.instagramapi.dto.response;

import com.example.instagramapi.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {

    private Long id;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;

    private UserResponse user;

    private boolean liked;
    private long likeCount;
    private long commentCount;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .user(UserResponse.from(post.getUser()))
                .createdAt(post.getCreatedAt())
                .liked(false)
                .likeCount(0)
                .commentCount(0)
                .build();

    }

    // liked, likeCount, commentCount 값이 들어 있을 시
    public static PostResponse from(
        Post post, boolean liked, long likeCount, long commentCount
    ) {
        return PostResponse.builder()
            .id(post.getId())
            .content(post.getContent())
            .imageUrl(post.getImageUrl())
            .user(UserResponse.from(post.getUser()))
            .createdAt(post.getCreatedAt())
            .liked(liked)
            .likeCount(likeCount)
            .commentCount(commentCount)
            .build();

    }

}
