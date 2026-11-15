package com.github.aabushmeleva.blog.service.mapper;

import com.github.aabushmeleva.blog.dto.CommentCreateRequest;
import com.github.aabushmeleva.blog.dto.CommentResponse;
import com.github.aabushmeleva.blog.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toNewComment(long postId, CommentCreateRequest request) {
        return new Comment(null, postId, request.text());
    }

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(comment.id(), comment.text(), comment.postId());
    }
}
