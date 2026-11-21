package com.github.aabushmeleva.blog.integration;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dao.TagDao;
import com.github.aabushmeleva.blog.model.Comment;
import com.github.aabushmeleva.blog.model.Post;
import com.github.aabushmeleva.blog.model.PostSearchQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostDaoIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PostDao postDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private CommentDao commentDao;

    @Test
    void shouldFilterByTitleAndAllTagsWithLimitAndOffset() {
        savePost("Horseshoe crab research", TAG_BIOLOGY, TAG_BIOETHICS);
        Post middle = savePost("Research on HORSESHOE crabs", TAG_BIOLOGY, TAG_BIOETHICS);
        savePost("Horseshoe crab conservation", TAG_BIOLOGY, TAG_BIOETHICS);

        savePost("Horseshoe crab anatomy", TAG_BIOLOGY);
        savePost("Horseshoe crab ethics", TAG_BIOETHICS);
        savePost("Marine conservation", TAG_BIOLOGY, TAG_BIOETHICS);

        PostSearchQuery query = new PostSearchQuery(
                "horseshoe",
                Set.of("biology", "bioethics")
        );

        List<Post> actual = postDao.findAll(query, 1, 1L);
        long total = postDao.count(query);

        assertEquals(List.of(middle), actual);
        assertEquals(3L, total);
    }

    @Test
    void shouldDeletePostWithItsTagsAndComments() {
        Post post = savePost("Horseshoe crab research", TAG_BIOLOGY, TAG_BIOETHICS);
        Comment comment = commentDao.save(
                new Comment(null, post.id(), "An important topic")
        );
        List<Long> postIds = List.of(post.id());

        assertFalse(tagDao.findByPostIds(postIds).isEmpty());
        assertTrue(commentDao.findById(comment.id()).isPresent());

        boolean deleted = postDao.deleteById(post.id());

        assertTrue(deleted);
        assertTrue(postDao.findById(post.id()).isEmpty());
        assertTrue(tagDao.findByPostIds(postIds).isEmpty());
        assertTrue(commentDao.findById(comment.id()).isEmpty());
    }

    // Test data and helper methods

    private static final String TAG_BIOLOGY = "biology";
    private static final String TAG_BIOETHICS = "bioethics";
    private static final String POST_TEXT = "Post text";

    private Post savePost(String title, String... tags) {
        Post post = postDao.save(
                new Post(null, title, "Post text", 0L, null)
        );
        tagDao.replaceTags(post.id(), List.of(tags));
        return post;
    }
}
