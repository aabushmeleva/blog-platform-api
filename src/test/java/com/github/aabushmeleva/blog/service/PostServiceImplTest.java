package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dao.TagDao;
import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostPageResponse;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.dto.PostUpdateRequest;
import com.github.aabushmeleva.blog.exception.InvalidTagException;
import com.github.aabushmeleva.blog.exception.PostNotFoundException;
import com.github.aabushmeleva.blog.model.Post;
import com.github.aabushmeleva.blog.model.PostSearchQuery;
import com.github.aabushmeleva.blog.service.event.PostDeletedEvent;
import com.github.aabushmeleva.blog.service.mapper.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostDao postDao;

    @Mock
    private TagDao tagDao;

    @Mock
    private CommentDao commentDao;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private PostService postService;

    @BeforeEach
    void setup() {
        postService = new PostServiceImpl(
                postDao,
                new PostMapper(),
                tagDao,
                commentDao,
                eventPublisher
        );
    }

    @Nested
    class CreateTests {

        @ParameterizedTest
        @MethodSource("com.github.aabushmeleva.blog.service.PostServiceImplTest#validTagCases")
        void shouldCreatePostWithNormalizedTags(
                List<String> inputTags,
                List<String> expectedTags
        ) {
            PostCreateRequest request = new PostCreateRequest(
                    POST_TITLE,
                    POST_TEXT,
                    inputTags
            );
            Post newPost = new Post(
                    null,
                    request.title(),
                    request.text(),
                    0L,
                    null
            );
            Post savedPost = new Post(
                    POST_ID,
                    request.title(),
                    request.text(),
                    0L,
                    null
            );
            PostResponse expected = new PostResponse(
                    POST_ID,
                    request.title(),
                    request.text(),
                    expectedTags,
                    0L,
                    0L
            );
            when(postDao.save(newPost)).thenReturn(savedPost);

            PostResponse actual = postService.create(request);

            assertEquals(expected, actual);
            verify(postDao).save(newPost);
            verify(tagDao).replaceTags(POST_ID, expectedTags);
        }

        @Test
        void shouldThrowInvalidTagExceptionWhenTagIsTooLong() {
            PostCreateRequest request = new PostCreateRequest(
                    POST_TITLE,
                    POST_TEXT,
                    List.of("A".repeat(65))
            );

            assertThrows(
                    InvalidTagException.class,
                    () -> postService.create(request)
            );

            verifyNoInteractions(postDao, tagDao, commentDao);
        }
    }

    @Nested
    class FindByIdTests {

        @ParameterizedTest
        @MethodSource("com.github.aabushmeleva.blog.service.PostServiceImplTest#postDetailsCases")
        void shouldReturnPostWithTagsAndCommentsCount(
                Map<Long, List<String>> tagsByPostId,
                Map<Long, Long> commentCountsByPostId,
                List<String> expectedTags,
                long expectedCommentsCount
        ) {
            Post post = existingPost();

            PostResponse expected = new PostResponse(
                    POST_ID,
                    POST_TITLE,
                    POST_TEXT,
                    expectedTags,
                    EXISTING_POST_LIKES_COUNT,
                    expectedCommentsCount
            );

            when(postDao.findById(POST_ID)).thenReturn(Optional.of(post));
            when(tagDao.findByPostIds(List.of(POST_ID)))
                    .thenReturn(tagsByPostId);
            when(commentDao.countByPostIds(List.of(POST_ID)))
                    .thenReturn(commentCountsByPostId);

            PostResponse actual = postService.findById(POST_ID);

            assertEquals(expected, actual);
            verify(postDao).findById(POST_ID);
            verify(tagDao).findByPostIds(List.of(POST_ID));
            verify(commentDao).countByPostIds(List.of(POST_ID));
        }

        @Test
        void shouldThrowPostNotFoundExceptionWhenPostDoesNotExist() {
            when(postDao.findById(POST_ID)).thenReturn(Optional.empty());

            assertThrows(
                    PostNotFoundException.class,
                    () -> postService.findById(POST_ID)
            );

            verify(postDao).findById(POST_ID);
            verifyNoInteractions(tagDao, commentDao);
        }
    }

    @Nested
    class UpdateTests {

        @ParameterizedTest
        @MethodSource("com.github.aabushmeleva.blog.service.PostServiceImplTest#validTagCases")
        void shouldUpdatePostWithNormalizedTags(
                List<String> inputTags,
                List<String> expectedTags
        ) {
            String imageName = "existing-image.png";

            Post existing = new Post(
                    POST_ID,
                    POST_TITLE,
                    POST_TEXT,
                    EXISTING_POST_LIKES_COUNT,
                    imageName
            );
            PostUpdateRequest request = updateRequest(inputTags);
            Post expectedUpdated = new Post(
                    POST_ID,
                    request.title(),
                    request.text(),
                    EXISTING_POST_LIKES_COUNT,
                    imageName
            );
            PostResponse expected = new PostResponse(
                    POST_ID,
                    request.title(),
                    request.text(),
                    expectedTags,
                    EXISTING_POST_LIKES_COUNT,
                    3L
            );
            when(postDao.findById(POST_ID)).thenReturn(Optional.of(existing));
            when(postDao.update(expectedUpdated)).thenReturn(true);
            when(commentDao.countByPostIds(List.of(POST_ID)))
                    .thenReturn(Map.of(POST_ID, 3L));

            PostResponse actual = postService.update(POST_ID, request);

            assertEquals(expected, actual);
            verify(postDao).findById(POST_ID);
            verify(postDao).update(expectedUpdated);
            verify(tagDao).replaceTags(POST_ID, expectedTags);
            verify(commentDao).countByPostIds(List.of(POST_ID));
        }

        @Test
        void shouldThrowPostNotFoundExceptionWhenUpdatingMissingPost() {
            PostUpdateRequest request = updateRequest(List.of("bioethics"));
            when(postDao.findById(POST_ID)).thenReturn(Optional.empty());

            assertThrows(
                    PostNotFoundException.class,
                    () -> postService.update(POST_ID, request)
            );
            verify(postDao).findById(POST_ID);
            verify(postDao, never()).update(any(Post.class));
            verifyNoInteractions(tagDao, commentDao);
        }

        @Test
        void shouldThrowPostNotFoundExceptionWhenPostUpdateReturnsFalse() {
            Post existing = existingPost();
            PostUpdateRequest request = updateRequest(List.of("bioethics"));
            Post expectedUpdated = new Post(
                    POST_ID,
                    request.title(),
                    request.text(),
                    existing.likesCount(),
                    existing.imageName()
            );
            when(postDao.findById(POST_ID)).thenReturn(Optional.of(existing));
            when(postDao.update(expectedUpdated)).thenReturn(false);

            assertThrows(
                    PostNotFoundException.class,
                    () -> postService.update(POST_ID, request)
            );
            verify(postDao).update(expectedUpdated);
            verifyNoInteractions(tagDao, commentDao);
        }

        @Test
        void shouldThrowInvalidTagExceptionWhenUpdatingWithTooLongTag() {
            PostUpdateRequest request = updateRequest(List.of("a".repeat(65)));
            when(postDao.findById(POST_ID))
                    .thenReturn(Optional.of(existingPost()));

            assertThrows(
                    InvalidTagException.class,
                    () -> postService.update(POST_ID, request)
            );

            verify(postDao).findById(POST_ID);
            verify(postDao, never()).update(any(Post.class));
            verifyNoInteractions(tagDao, commentDao);
        }
    }

    @Nested
    class DeleteTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = "existing-image.png")
        void shouldDeletePostAndPublishEvent(String imageName) {
            when(postDao.findImageName(POST_ID))
                    .thenReturn(Optional.ofNullable(imageName));
            when(postDao.deleteById(POST_ID)).thenReturn(true);

            postService.delete(POST_ID);

            InOrder inOrder = inOrder(postDao, eventPublisher);
            inOrder.verify(postDao).findImageName(POST_ID);
            inOrder.verify(postDao).deleteById(POST_ID);
            inOrder.verify(eventPublisher)
                    .publishEvent(new PostDeletedEvent(POST_ID, imageName));
        }

        @Test
        void shouldThrowPostNotFoundExceptionWhenPostDeletionReturnsFalse() {
            when(postDao.findImageName(POST_ID)).thenReturn(Optional.empty());
            when(postDao.deleteById(POST_ID)).thenReturn(false);

            assertThrows(
                    PostNotFoundException.class,
                    () -> postService.delete(POST_ID)
            );

            verify(postDao).findImageName(POST_ID);
            verify(postDao).deleteById(POST_ID);
            verifyNoInteractions(eventPublisher);
        }
    }

    @Nested
    class IncrementLikesTests {

        @ParameterizedTest
        @ValueSource(longs = {1L, 8L, 100L})
        void shouldReturnUpdatedLikesCount(long updatedLikesCount) {
            when(postDao.incrementLikes(POST_ID))
                    .thenReturn(OptionalLong.of(updatedLikesCount));

            long actual = postService.incrementLikes(POST_ID);

            assertEquals(updatedLikesCount, actual);
            verify(postDao).incrementLikes(POST_ID);
            verifyNoInteractions(tagDao, commentDao, eventPublisher);
        }

        @Test
        void shouldThrowPostNotFoundExceptionWhenLikingMissingPost() {
            when(postDao.incrementLikes(POST_ID))
                    .thenReturn(OptionalLong.empty());

            assertThrows(
                    PostNotFoundException.class,
                    () -> postService.incrementLikes(POST_ID)
            );

            verify(postDao).incrementLikes(POST_ID);
            verifyNoInteractions(tagDao, commentDao, eventPublisher);
        }
    }

    @Nested
    class FindPageTests {

        @Test
        void shouldReturnPostPageWithTagsAndCommentsCounts() {
            PostSearchQuery query = new PostSearchQuery("Horseshoe", Set.of("bioethics"));
            Post first = existingPost();
            Post second = new Post(POST_ID - 1, "Another post", "Another text", 2L, null);
            List<Long> ids = List.of(first.id(), second.id());

            when(postDao.count(query)).thenReturn(2L);
            when(postDao.findAll(query, 5, 0L)).thenReturn(List.of(first, second));
            when(tagDao.findByPostIds(ids)).thenReturn(Map.of(first.id(), List.of("bioethics")));
            when(commentDao.countByPostIds(ids)).thenReturn(Map.of(first.id(), 3L));

            PostPageResponse actual = postService.findPage("Horseshoe #Bioethics", 1, 5);

            PostPageResponse expected = new PostPageResponse(
                    List.of(
                            new PostResponse(first.id(), first.title(), first.text(),
                                    List.of("bioethics"), first.likesCount(), 3L),
                            new PostResponse(second.id(), second.title(), second.text(),
                                    List.of(), second.likesCount(), 0L)
                    ),
                    false, false, 1
            );
            assertEquals(expected, actual);
            verify(tagDao).findByPostIds(ids);
            verify(commentDao).countByPostIds(ids);
        }

        @Test
        void shouldCalculatePaginationForMiddlePage() {
            PostSearchQuery query = new PostSearchQuery("", Set.of());
            List<Long> ids = List.of(POST_ID);

            when(postDao.count(query)).thenReturn(15L);
            when(postDao.findAll(query, 5, 5L)).thenReturn(List.of(existingPost()));
            when(tagDao.findByPostIds(ids)).thenReturn(Map.of());
            when(commentDao.countByPostIds(ids)).thenReturn(Map.of());

            PostPageResponse actual = postService.findPage("", 2, 5);

            assertTrue(actual.hasPrev());
            assertTrue(actual.hasNext());
            assertEquals(3, actual.lastPage());
        }
    }

    // Test data and helper methods

    private static final long POST_ID = 42L;
    private static final String POST_TITLE = "Horseshoe crabs and bioethics";
    private static final String POST_TEXT = "A post about horseshoe crab uses";
    private static final long EXISTING_POST_LIKES_COUNT = 7L;

    private static Post existingPost() {
        return new Post(
                POST_ID,
                POST_TITLE,
                POST_TEXT,
                EXISTING_POST_LIKES_COUNT,
                null
        );
    }

    private static PostUpdateRequest updateRequest(List<String> tags) {
        return new PostUpdateRequest(
                POST_ID,
                "Updated title",
                "Updated text",
                tags
        );
    }

    private static Stream<Arguments> validTagCases() {
        String inputMaxLengthTag = "A".repeat(64);
        String expectedMaxLengthTag = "a".repeat(64);

        return Stream.of(
                Arguments.of(
                        List.of("#BIOETHICS", " horseshoecrab ", "bioethics"),
                        List.of("bioethics", "horseshoecrab")
                ),
                Arguments.of(
                        List.of(inputMaxLengthTag),
                        List.of(expectedMaxLengthTag)
                ),
                Arguments.of(
                        List.<String>of(),
                        List.<String>of()
                )
        );
    }

    private static Stream<Arguments> postDetailsCases() {
        return Stream.of(
                Arguments.of(
                        Map.of(POST_ID, List.of("bioethics", "horseshoecrab")),
                        Map.of(POST_ID, 3L),
                        List.of("bioethics", "horseshoecrab"),
                        3L
                ),
                Arguments.of(
                        Map.of(),
                        Map.of(POST_ID, 3L),
                        List.of(),
                        3L
                ),
                Arguments.of(
                        Map.of(POST_ID, List.of("bioethics")),
                        Map.of(),
                        List.of("bioethics"),
                        0L
                ),
                Arguments.of(
                        Map.of(),
                        Map.of(),
                        List.of(),
                        0L
                )
        );
    }
}
