package com.github.aabushmeleva.blog.service.mapper;

import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    private static final long INITIAL_LIKES_COUNT = 0;
    private static final int PREVIEW_LENGTH = 128;
    private static final String ELLIPSIS = "…";

    public Post toNewPost(PostCreateRequest request) {
        return new Post(null, request.title(), request.text(), INITIAL_LIKES_COUNT, null);
    }

    public PostResponse toResponse(Post post, List<String> tags, long commentsCount) {
        return new PostResponse(
                post.id(),
                post.title(),
                post.text(),
                tags,
                post.likesCount(),
                commentsCount
        );
    }

    public PostResponse toFeedResponse(Post post, List<String> tags, long commentsCount) {
        return new PostResponse(post.id(), post.title(), preview(post.text()),
                tags, post.likesCount(), commentsCount);
    }

    private static String preview(String text) {
        if (text.codePointCount(0, text.length()) <= PREVIEW_LENGTH) {
            return text;
        }
        return text.substring(0, text.offsetByCodePoints(0, PREVIEW_LENGTH)) + ELLIPSIS;
    }
}
