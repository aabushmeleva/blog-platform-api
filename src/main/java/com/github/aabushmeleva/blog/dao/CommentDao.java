package com.github.aabushmeleva.blog.dao;

import com.github.aabushmeleva.blog.model.Comment;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentDao {
    List<Comment> findByPostId(long postId);

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    boolean update(Comment comment);

    boolean deleteByIdAndPostId(long id, long postId);

    Map<Long, Long> countByPostIds(Collection<Long> postIds);
}
