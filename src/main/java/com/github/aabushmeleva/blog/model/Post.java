package com.github.aabushmeleva.blog.model;

public record Post(
        Long id,
        String title,
        String text,
        long likesCount,
        String imageName
) {
}
