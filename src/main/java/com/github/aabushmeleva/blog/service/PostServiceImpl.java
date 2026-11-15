package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dao.TagDao;
import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostPageResponse;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.dto.PostUpdateRequest;
import com.github.aabushmeleva.blog.exception.PostNotFoundException;
import com.github.aabushmeleva.blog.service.mapper.PostMapper;
import com.github.aabushmeleva.blog.model.Post;
import com.github.aabushmeleva.blog.service.tag.TagNormalizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final long NEW_POST_COMMENTS_COUNT = 0;

    private final PostDao postDao;
    private final PostMapper postMapper;
    private final TagDao tagDao;
    private final CommentDao commentDao;

    public PostServiceImpl(PostDao postDao, PostMapper postMapper, TagDao tagDao, CommentDao commentDao) {
        this.postDao = postDao;
        this.postMapper = postMapper;
        this.tagDao = tagDao;
        this.commentDao = commentDao;
    }

    @Override
    @Transactional
    public PostResponse create(PostCreateRequest request) {
        Post saved = postDao.save(postMapper.toNewPost(request));
        List<String> tags = TagNormalizer.normalize(request.tags());
        tagDao.replaceTags(saved.id(), tags);
        return postMapper.toResponse(saved, tags, NEW_POST_COMMENTS_COUNT);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse findById(long id) {
        Post post = postDao.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return postMapper.toResponse(post, findTags(id), countComments(id));
    }

    @Override
    public PostPageResponse findPage(String search, int pageNumber, int pageSize) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public PostResponse update(long id, PostUpdateRequest request) {
        Post updated = postDao.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id))
                .withContent(request.title(), request.text());
        if (!postDao.update(updated)) {
            throw new PostNotFoundException(id);
        }
        List<String> tags = TagNormalizer.normalize(request.tags());
        tagDao.replaceTags(id, tags);
        return postMapper.toResponse(updated, tags, countComments(id));
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (!postDao.deleteById(id)) {
            throw new PostNotFoundException(id);
        }
    }

    @Override
    @Transactional
    public long incrementLikes(long id) {
        return postDao.incrementLikes(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    private List<String> findTags(long postId) {
        return tagDao.findByPostIds(List.of(postId)).getOrDefault(postId, List.of());
    }

    private long countComments(long postId) {
        return commentDao.countByPostIds(List.of(postId)).getOrDefault(postId, 0L);
    }
}
