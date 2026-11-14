package com.dzhenakova.blog.platform.api.dto;

public record CommentCreateRequest(
        String text,
        Long postId
) {
}
