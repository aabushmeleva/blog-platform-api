package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dto.CommentCreateRequest;
import com.github.aabushmeleva.blog.dto.CommentResponse;
import com.github.aabushmeleva.blog.dto.CommentUpdateRequest;
import com.github.aabushmeleva.blog.exception.CommentNotFoundException;
import com.github.aabushmeleva.blog.exception.PostNotFoundException;
import com.github.aabushmeleva.blog.model.Comment;
import com.github.aabushmeleva.blog.service.mapper.CommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentDao commentDao;

    @Mock
    private PostDao postDao;

    private CommentService commentService;

    @BeforeEach
    void setup() {
        commentService = new CommentServiceImpl(
                commentDao,
                postDao,
                new CommentMapper()
        );
    }

    @Nested
    class CreateTests {

        @Test
        void shouldCreateComment() {
            CommentCreateRequest request = new CommentCreateRequest(COMMENT_TEXT, POST_ID);
            Comment newComment = new Comment(null, POST_ID, COMMENT_TEXT);
            Comment savedComment = new Comment(COMMENT_ID, POST_ID, COMMENT_TEXT);
            CommentResponse expected = new CommentResponse(COMMENT_ID, COMMENT_TEXT, POST_ID);

            when(postDao.existsById(POST_ID)).thenReturn(true);
            when(commentDao.save(newComment)).thenReturn(savedComment);

            CommentResponse actual = commentService.create(POST_ID, request);

            assertEquals(expected, actual);
            verify(postDao).existsById(POST_ID);
            verify(commentDao).save(newComment);
        }

        @Test
        void shouldThrowPostNotFoundExceptionWhenCreatingForMissingPost() {
            CommentCreateRequest request = new CommentCreateRequest(COMMENT_TEXT, POST_ID);
            when(postDao.existsById(POST_ID)).thenReturn(false);

            assertThrows(PostNotFoundException.class, () -> commentService.create(POST_ID, request));
            verifyNoInteractions(commentDao);
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void shouldUpdateCommentAndPreservePostId() {
            Comment existing = new Comment(COMMENT_ID, POST_ID, COMMENT_TEXT);
            CommentUpdateRequest request = new CommentUpdateRequest(COMMENT_ID, UPDATED_TEXT, POST_ID);
            Comment updated = new Comment(COMMENT_ID, POST_ID, UPDATED_TEXT);
            CommentResponse expected = new CommentResponse(COMMENT_ID, UPDATED_TEXT, POST_ID);

            when(commentDao.findById(COMMENT_ID)).thenReturn(Optional.of(existing));
            when(commentDao.update(updated)).thenReturn(true);

            CommentResponse actual = commentService.update(COMMENT_ID, request);

            assertEquals(expected, actual);
            verify(commentDao).update(updated);
        }

        @Test
        void shouldThrowCommentNotFoundExceptionWhenUpdatingMissingComment() {
            CommentUpdateRequest request = new CommentUpdateRequest(COMMENT_ID, UPDATED_TEXT, POST_ID);
            when(commentDao.findById(COMMENT_ID)).thenReturn(Optional.empty());

            assertThrows(CommentNotFoundException.class, () -> commentService.update(COMMENT_ID, request));
            verify(commentDao, never()).update(any(Comment.class));
        }


        @Test
        void shouldThrowCommentNotFoundExceptionWhenUpdateReturnsFalse() {
            Comment existing = new Comment(COMMENT_ID, POST_ID, COMMENT_TEXT);
            CommentUpdateRequest request = new CommentUpdateRequest(COMMENT_ID, UPDATED_TEXT, POST_ID);
            Comment updated = new Comment(COMMENT_ID, POST_ID, UPDATED_TEXT);

            when(commentDao.findById(COMMENT_ID)).thenReturn(Optional.of(existing));
            when(commentDao.update(updated)).thenReturn(false);

            assertThrows(CommentNotFoundException.class, () -> commentService.update(COMMENT_ID, request));
            verify(commentDao).update(updated);
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        void shouldReturnCommentById() {
            Comment comment = new Comment(COMMENT_ID, POST_ID, COMMENT_TEXT);
            CommentResponse expected = new CommentResponse(COMMENT_ID, COMMENT_TEXT, POST_ID);

            when(commentDao.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

            CommentResponse actual = commentService.findById(POST_ID, COMMENT_ID);

            assertEquals(expected, actual);
            verify(commentDao).findById(COMMENT_ID);
        }

        @Test
        void shouldThrowCommentNotFoundExceptionWhenCommentBelongsToAnotherPost() {
            long anotherPostId = POST_ID + 1;
            Comment comment = new Comment(COMMENT_ID, anotherPostId, COMMENT_TEXT);

            when(commentDao.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

            assertThrows(CommentNotFoundException.class, () -> commentService.findById(POST_ID, COMMENT_ID));
        }

        @Test
        void shouldThrowCommentNotFoundExceptionWhenCommentDoesNotExist() {
            when(commentDao.findById(COMMENT_ID)).thenReturn(Optional.empty());

            assertThrows(CommentNotFoundException.class, () -> commentService.findById(POST_ID, COMMENT_ID));
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void shouldDeleteComment() {
            when(commentDao.deleteByIdAndPostId(COMMENT_ID, POST_ID)).thenReturn(true);

            commentService.delete(POST_ID, COMMENT_ID);

            verify(commentDao).deleteByIdAndPostId(COMMENT_ID, POST_ID);
        }

        @Test
        void shouldThrowCommentNotFoundExceptionWhenDeletionReturnsFalse() {
            when(commentDao.deleteByIdAndPostId(COMMENT_ID, POST_ID)).thenReturn(false);

            assertThrows(CommentNotFoundException.class, () -> commentService.delete(POST_ID, COMMENT_ID));
            verify(commentDao).deleteByIdAndPostId(COMMENT_ID, POST_ID);
        }
    }

    // Test data and helper methods

    private static final long POST_ID = 42L;
    private static final long COMMENT_ID = 15L;
    private static final String COMMENT_TEXT = "Original comment about poor horseshoes crabs";
    private static final String UPDATED_TEXT = "Updated comment with feeling so bad for those poor horseshoe crabs";
}
