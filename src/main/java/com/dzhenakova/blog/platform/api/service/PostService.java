package com.dzhenakova.blog.platform.api.service;

import com.dzhenakova.blog.platform.api.dto.PostCreateRequest;
import com.dzhenakova.blog.platform.api.dto.PostPageResponse;
import com.dzhenakova.blog.platform.api.dto.PostResponse;
import com.dzhenakova.blog.platform.api.dto.PostUpdateRequest;

public interface PostService {

    PostPageResponse findPage(String search, int pageNumber, int pageSize);

    PostResponse findById(long id);

    PostResponse create(PostCreateRequest request);

    PostResponse update(long id, PostUpdateRequest request);

    void delete(long id);

    long incrementLikes(long id);
}
