package com.dzhenakova.blog.platform.api.model;

public record Post(
        Long id,
        String title,
        String text,
        long likesCount,
        String imageName
) {
}
