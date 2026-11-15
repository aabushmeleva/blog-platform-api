package com.dzhenakova.blog.platform.api.service;

import com.dzhenakova.blog.platform.api.dto.CommentCreateRequest;
import com.dzhenakova.blog.platform.api.dto.CommentResponse;
import com.dzhenakova.blog.platform.api.dto.CommentUpdateRequest;

import java.util.List;

public interface CommentService {

    List<CommentResponse> findByPostId(long postId);

    CommentResponse findById(long commentId);

    CommentResponse create(long postId, CommentCreateRequest request);

    CommentResponse update(long commentId, CommentUpdateRequest request);

    void delete(long commentId);
}
