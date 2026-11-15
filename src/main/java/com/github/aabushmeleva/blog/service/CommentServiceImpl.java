package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dto.CommentCreateRequest;
import com.github.aabushmeleva.blog.dto.CommentResponse;
import com.github.aabushmeleva.blog.dto.CommentUpdateRequest;
import com.github.aabushmeleva.blog.exception.CommentNotFoundException;
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
    @Transactional(readOnly = true)
    public List<CommentResponse> findByPostId(long postId) {
        requirePostExists(postId);
        return commentDao.findByPostId(postId).stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponse findById(long postId, long commentId) {
        return commentDao.findById(commentId)
                .filter(comment -> comment.postId() == postId)
                .map(commentMapper::toResponse)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    @Override
    @Transactional
    public CommentResponse create(long postId, CommentCreateRequest request) {
        requirePostExists(postId);
        Comment saved = commentDao.save(commentMapper.toNewComment(postId, request));
        return commentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CommentResponse update(long commentId, CommentUpdateRequest request) {
        Comment existing = commentDao.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        Comment updated = existing.withText(request.text());
        if (!commentDao.update(updated)) {
            throw new CommentNotFoundException(commentId);
        }
        return commentMapper.toResponse(updated);
    }

    @Override
    public void delete(long commentId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private void requirePostExists(long postId) {
        if (!postDao.existsById(postId)) {
            throw new CommentNotFoundException(postId);
        }
    }
}
