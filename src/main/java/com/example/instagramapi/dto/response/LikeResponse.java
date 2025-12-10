package com.example.instagramapi.dto.response;

import lombok.Builder;
import lombok.Getter;

// { liked : true, likeCount : 10 }
@Getter
@Builder
public class LikeResponse {

    private boolean liked;

    private long likeCount;

    public static LikeResponse of(boolean liked, long likeCount) {
        return LikeResponse.builder()
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }
}
