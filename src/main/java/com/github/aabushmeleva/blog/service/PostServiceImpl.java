package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostPageResponse;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.dto.PostUpdateRequest;
import com.github.aabushmeleva.blog.exception.NotFoundException;
import com.github.aabushmeleva.blog.service.mapper.PostMapper;
import com.github.aabushmeleva.blog.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final long NEW_POST_COMMENTS_COUNT = 0;

    private final PostDao postDao;
    private final PostMapper postMapper;

    public PostServiceImpl(PostDao postDao, PostMapper postMapper) {
        this.postDao = postDao;
        this.postMapper = postMapper;
    }

    @Override
    public PostResponse create(PostCreateRequest request) {
        Post saved = postDao.save(postMapper.toNewPost(request));
        // TODO: save & return tags
        return postMapper.toResponse(saved, List.of(), NEW_POST_COMMENTS_COUNT);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse findById(long id) {
        Post post = postDao.findById(id)
                .orElseThrow(() -> postNotFound(id));
        // TODO: load tags and comments count
        return postMapper.toResponse(post, List.of(), 0);
    }

    @Override
    public PostPageResponse findPage(String search, int pageNumber, int pageSize) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public PostResponse update(long id, PostUpdateRequest request) {
        Post updated = postDao.findById(id)
                .orElseThrow(() -> postNotFound(id))
                .withContent(request.title(), request.text());

        if (!postDao.update(updated)) {
            throw postNotFound(id);
        }
        // TODO: return tags and comments count
        return postMapper.toResponse(updated, List.of(), 0);
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (!postDao.deleteById(id)) {
            throw postNotFound(id);
        }
    }

    @Override
    public long incrementLikes(long id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static NotFoundException postNotFound(long id) {
        return new NotFoundException("Post with id " + id + " not found");
    }
}
