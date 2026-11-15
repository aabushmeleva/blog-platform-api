package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostPageResponse;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.dto.PostUpdateRequest;

public interface PostService {

    PostPageResponse findPage(String search, int pageNumber, int pageSize);

    PostResponse findById(long id);

    PostResponse create(PostCreateRequest request);

    PostResponse update(long id, PostUpdateRequest request);

    void delete(long id);

    long incrementLikes(long id);
}
