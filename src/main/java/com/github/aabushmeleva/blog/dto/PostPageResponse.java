package com.github.aabushmeleva.blog.dto;

import java.util.List;

public record PostPageResponse(
        List<PostResponse> posts,
        boolean hasPrev,
        boolean hasNext,
        int lastPage
) {
    public PostPageResponse {
        List.copyOf(posts);
    }
}
