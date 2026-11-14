package com.dzhenakova.blog.platform.api.dto;

public record CommentUpdateRequest(
        Long id,
        String text,
        Long postId
) {
}
