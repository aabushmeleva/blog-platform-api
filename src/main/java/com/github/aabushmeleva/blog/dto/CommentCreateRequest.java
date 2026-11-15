package com.github.aabushmeleva.blog.dto;

public record CommentCreateRequest(
        String text,
        Long postId
) {
}
