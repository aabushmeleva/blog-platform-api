package com.github.aabushmeleva.blog.dto;

import java.util.List;

public record PostUpdateRequest(
        Long id,
        String title,
        String text,
        List<String> tags
) {
    public PostUpdateRequest {
        tags = tags == null ? List.of() : List.copyOf(tags);
    }
}
