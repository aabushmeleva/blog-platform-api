package com.github.aabushmeleva.blog.service.mapper;

import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    private static final long INITIAL_LIKES_COUNT = 0;

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
}
