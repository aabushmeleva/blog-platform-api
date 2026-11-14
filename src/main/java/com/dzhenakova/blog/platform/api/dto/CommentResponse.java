package com.dzhenakova.blog.platform.api.dto;

public record CommentResponse(
        Long id,
        String text,
        Long postId
) {
}
