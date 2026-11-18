package com.github.aabushmeleva.blog.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank
        String text,
        Long postId
) {
}
