package com.example.instagramapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 200, message = "내용은 2000자까지.")
    private String content;

    private String imageUrl;

}
