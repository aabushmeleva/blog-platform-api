package com.github.aabushmeleva.blog.dto;

import java.util.List;

public record PostResponse(
        Long id,
        String title,
        String text,
        List<String> tags,
        long likesCount,
        long commentsCount
) {
    public PostResponse {
        List.copyOf(tags);
    }
}
