package com.github.aabushmeleva.blog.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        Long id,
        @NotBlank
        String text,
        Long postId
) {
}
