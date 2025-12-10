package com.example.instagramapi.dto.response;

import com.example.instagramapi.entity.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {

    private Long id;

    private String content;

    private UserResponse author;

    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .author(UserResponse.from(comment.getUser()))
            .createdAt(comment.getCreatedAt())
            .build();
     }

}
