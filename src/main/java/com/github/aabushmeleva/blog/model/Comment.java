package com.github.aabushmeleva.blog.model;

public record Comment(
        Long id,
        Long postId,
        String text
) {
    public Comment withText(String text) {
        return new Comment(id, postId, text);
    }
}
