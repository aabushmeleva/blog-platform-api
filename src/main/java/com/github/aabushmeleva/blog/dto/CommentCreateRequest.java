package com.github.aabushmeleva.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotBlank
        String text,
        @NotNull
        Long postId
) {
}
