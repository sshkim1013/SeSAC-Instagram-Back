package com.example.instagramapi.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

// { content: [게시물 1, 게시물 2, 게시물 3, 게시물 4], hasNext: true, page: 2, size: 5 }
@Getter
@Builder
public class SliceResponse<T> {

    private List<T> content;

    private boolean hasNext;

    private int page;

    private int size;

    public static <T> SliceResponse<T> from(Slice<?> slice, List<T> content) {
        return SliceResponse.<T>builder()
                .content(content)
                .hasNext(slice.hasNext())
                .page(slice.getNumber())
                .size(slice.getSize())
                .build();
    }

}
