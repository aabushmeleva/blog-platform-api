package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dao.TagDao;
import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostPageResponse;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.dto.PostUpdateRequest;
import com.github.aabushmeleva.blog.exception.PostNotFoundException;
import com.github.aabushmeleva.blog.model.PostSearchQuery;
import com.github.aabushmeleva.blog.service.event.PostDeletedEvent;
import com.github.aabushmeleva.blog.service.mapper.PostMapper;
import com.github.aabushmeleva.blog.model.Post;
import com.github.aabushmeleva.blog.service.tag.TagNormalizer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    private static final long NEW_POST_COMMENTS_COUNT = 0;

    private final PostDao postDao;
    private final PostMapper postMapper;
    private final TagDao tagDao;
    private final CommentDao commentDao;
    private final ApplicationEventPublisher eventPublisher;

    public PostServiceImpl(PostDao postDao, PostMapper postMapper, TagDao tagDao, CommentDao commentDao, ApplicationEventPublisher eventPublisher) {
        this.postDao = postDao;
        this.postMapper = postMapper;
        this.tagDao = tagDao;
        this.commentDao = commentDao;
        this.eventPublisher = eventPublisher;
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
    @Transactional(readOnly = true)
    public PostPageResponse findPage(String search, int pageNumber, int pageSize) {
        PostSearchQuery query = new PostSearchQuery("", Set.of());
        long total = postDao.count(query);
        int lastPage = (int) Math.max(1, (total + pageSize - 1) / pageSize);
        long offset = (long) (pageNumber - 1) * pageSize;

        List<Post> posts = postDao.findAll(query, pageSize, offset);
        List<Long> ids = posts.stream().map(Post::id).toList();
        Map<Long, List<String>> tags = tagDao.findByPostIds(ids);
        Map<Long, Long> commentCounts = commentDao.countByPostIds(ids);

        List<PostResponse> items = posts.stream()
                .map(post -> postMapper.toFeedResponse(post,
                        tags.getOrDefault(post.id(), List.of()),
                        commentCounts.getOrDefault(post.id(), 0L)))
                .toList();
        return new PostPageResponse(items, pageNumber > 1, pageNumber < lastPage, lastPage);
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
        String imageName = postDao.findImageName(id).orElse(null);
        if (!postDao.deleteById(id)) {
            throw new PostNotFoundException(id);
        }
        eventPublisher.publishEvent(new PostDeletedEvent(id, imageName));
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
