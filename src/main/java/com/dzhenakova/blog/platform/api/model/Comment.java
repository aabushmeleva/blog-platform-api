package com.dzhenakova.blog.platform.api.model;

public record Comment(
        Long id,
        Long postId,
        String text
) {
}
