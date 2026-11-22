package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.exception.EmptyImageException;
import com.github.aabushmeleva.blog.storage.ImageStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostImageServiceImplTest {

    @Mock
    private PostDao postDao;

    @Mock
    private ImageStorage imageStorage;

    private PostImageService postImageService;

    @BeforeEach
    void setup() {
        postImageService = new PostImageServiceImpl(
                postDao,
                imageStorage
        );
    }

    @Test
    void shouldReplacePostImageAndDeleteOldFile() throws IOException {
        long postId = 42L;
        String oldImageName = "horseshoe_crab.png";
        String newImageName = "new_big_horseshoe_crab.png";

        MockMultipartFile image = imageFile(new byte[]{1, 2, 3});
        String originalFilename = image.getOriginalFilename();

        when(postDao.findImageName(postId)).thenReturn(Optional.of(oldImageName));
        when(imageStorage.save(any(InputStream.class), eq(originalFilename))).thenReturn(newImageName);
        when(postDao.updateImageName(postId, newImageName)).thenReturn(true);

        postImageService.updateImage(postId, image);

        InOrder inOrder = inOrder(imageStorage, postDao);
        inOrder.verify(imageStorage).save(any(InputStream.class), eq(originalFilename));
        inOrder.verify(postDao).updateImageName(postId, newImageName);
        inOrder.verify(imageStorage).delete(oldImageName);
    }

    @Test
    void shouldRejectEmptyImageWithoutCallingDaoOrStorage() {
        MockMultipartFile image = imageFile(new byte[0]);

        assertThrows(EmptyImageException.class, () -> postImageService.updateImage(42L, image));
        verifyNoInteractions(postDao, imageStorage);
    }

    // Test data and helper methods

    private static MockMultipartFile imageFile(byte[] content) {
        return new MockMultipartFile(
                "image",
                "horseshoe_crab.png",
                "image/png",
                content
        );
    }
}
