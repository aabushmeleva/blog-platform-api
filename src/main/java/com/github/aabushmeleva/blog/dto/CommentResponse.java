package com.github.aabushmeleva.blog.dto;

public record CommentResponse(
        Long id,
        String text,
        Long postId
) {
}
