package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dto.CommentCreateRequest;
import com.github.aabushmeleva.blog.dto.CommentResponse;
import com.github.aabushmeleva.blog.dto.CommentUpdateRequest;
import com.github.aabushmeleva.blog.exception.NotFoundException;
import com.github.aabushmeleva.blog.model.Comment;
import com.github.aabushmeleva.blog.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final PostDao postDao;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponse> findByPostId(long postId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public CommentResponse findById(long commentId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    @Transactional
    public CommentResponse create(long postId, CommentCreateRequest request) {
        if (!postDao.existsById(postId)) {
            throw new NotFoundException("Post not found: " + postId);
        }
        Comment saved = commentDao.save(commentMapper.toNewComment(postId, request));
        return commentMapper.toResponse(saved);
    }

    @Override
    public CommentResponse update(long commentId, CommentUpdateRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(long commentId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
