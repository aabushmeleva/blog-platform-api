package com.github.aabushmeleva.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostCreateRequest(
        @NotBlank @Size(max = 255)
        String title,
        @NotBlank
        String text,
        List<String> tags
) {
    public PostCreateRequest {
        tags = tags == null ? List.of() : List.copyOf(tags);
    }
}
