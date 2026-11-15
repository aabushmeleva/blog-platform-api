package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dto.CommentCreateRequest;
import com.github.aabushmeleva.blog.dto.CommentResponse;
import com.github.aabushmeleva.blog.dto.CommentUpdateRequest;

import java.util.List;

public interface CommentService {

    List<CommentResponse> findByPostId(long postId);

    CommentResponse findById(long postId, long commentId);

    CommentResponse create(long postId, CommentCreateRequest request);

    CommentResponse update(long commentId, CommentUpdateRequest request);

    void delete(long commentId);
}
