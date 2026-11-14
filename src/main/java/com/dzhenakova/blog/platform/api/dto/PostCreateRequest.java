package com.dzhenakova.blog.platform.api.dto;

import java.util.List;

public record PostCreateRequest(
        String title,
        String text,
        List<String> tags
) {
    public PostCreateRequest {
        tags = tags == null ? List.of() : List.copyOf(tags);
    }
}
