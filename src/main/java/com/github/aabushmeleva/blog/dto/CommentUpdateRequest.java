package com.github.aabushmeleva.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentUpdateRequest(
        @NotNull
        Long id,
        @NotBlank
        String text,
        @NotNull
        Long postId
) {
}
