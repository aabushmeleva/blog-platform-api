package com.github.aabushmeleva.blog.dto;

public record CommentUpdateRequest(
        Long id,
        String text,
        Long postId
) {
}
