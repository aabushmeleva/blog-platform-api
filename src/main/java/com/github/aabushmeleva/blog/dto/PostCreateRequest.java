package com.github.aabushmeleva.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostCreateRequest(
        @NotBlank @Size(max = 255)
        String title,
        @NotBlank
        String text,
        @NotNull
        List<@NotNull String> tags
) { }
