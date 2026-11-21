package com.github.aabushmeleva.blog.integration;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dao.TagDao;
import com.github.aabushmeleva.blog.model.Comment;
import com.github.aabushmeleva.blog.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostMvcIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostDao postDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private CommentDao commentDao;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void shouldReturnPostPageWithContractFields() throws Exception {
        Post post = savePost();
        tagDao.replaceTags(post.id(), List.of(TAG_BIOETHICS));
        commentDao.save(
                new Comment(null, post.id(), "An important topic")
        );

        mockMvc.perform(get("/api/posts")
                        .param("search", "")
                        .param("pageNumber", "1")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(1))
                .andExpect(jsonPath("$.posts[0].id")
                        .value(post.id().intValue()))
                .andExpect(jsonPath("$.posts[0].title").value(POST_TITLE))
                .andExpect(jsonPath("$.posts[0].text").value(POST_TEXT))
                .andExpect(jsonPath("$.posts[0].tags")
                        .value(contains(TAG_BIOETHICS)))
                .andExpect(jsonPath("$.posts[0].likesCount").value(7))
                .andExpect(jsonPath("$.posts[0].commentsCount").value(1))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.lastPage").value(1));
    }

    @Test
    void shouldReturnNotFoundForMissingPost() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenPathAndBodyIdsDoNotMatch()
            throws Exception {
        Post post = savePost();

        String requestBody = """
                {
                  "id": %d,
                  "title": "Horseshoe crabs and bioethics",
                  "text": "An updated post about horseshoe crab uses",
                  "tags": []
                }
                """.formatted(post.id() + 1);

        mockMvc.perform(put("/api/posts/{id}", post.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(
                                MediaType.APPLICATION_PROBLEM_JSON
                        ))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    // Test data and helper methods

    private static final String POST_TITLE = "Horseshoe crab research";
    private static final String POST_TEXT = "A post about scientists study the unique immune system of horseshoe crabs";
    private static final String TAG_BIOETHICS = "bioethics";

    private Post savePost() {
        return postDao.save(
                new Post(null, POST_TITLE, POST_TEXT, 7L, null)
        );
    }
}
