package com.github.aabushmeleva.blog.model;

public record Comment(
        Long id,
        Long postId,
        String text
) {
}
