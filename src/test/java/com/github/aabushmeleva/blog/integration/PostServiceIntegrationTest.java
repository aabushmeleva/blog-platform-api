package com.github.aabushmeleva.blog.integration;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dao.TagDao;
import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.model.Post;
import com.github.aabushmeleva.blog.service.PostService;
import com.github.aabushmeleva.blog.service.PostServiceImpl;
import com.github.aabushmeleva.blog.service.mapper.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PostDao postDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private PostService postService;

    @Test
    void shouldCreatePostWithTags() {
        PostCreateRequest request = new PostCreateRequest(
                "Horseshoe crab research",
                "A post about the immune system of horseshoe crabs",
                List.of("biology", "bioethics")
        );
        PostResponse response = postService.create(request);

        assertNotNull(response.id());

        Post saved = postDao.findById(response.id()).orElseThrow();

        assertEquals(request.title(), saved.title());
        assertEquals(request.text(), saved.text());
        assertEquals(0L, saved.likesCount());

        List<String> savedTags = tagDao
                .findByPostIds(List.of(response.id()))
                .get(response.id());

        assertEquals(List.of("bioethics", "biology"), savedTags);
    }
}
