package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dto.PostImageContent;
import com.github.aabushmeleva.blog.exception.EmptyImageException;
import com.github.aabushmeleva.blog.exception.ImageNotFoundException;
import com.github.aabushmeleva.blog.exception.NotFoundException;
import com.github.aabushmeleva.blog.exception.PostNotFoundException;
import com.github.aabushmeleva.blog.exception.UnsupportedImageTypeException;
import com.github.aabushmeleva.blog.storage.ImageExtensions;
import com.github.aabushmeleva.blog.storage.ImageStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final PostDao postDao;
    private final ImageStorage imageStorage;

    @Override
    public void updateImage(long postId, MultipartFile image) {
        if (image.isEmpty()) {
            throw new EmptyImageException();
        }
        Optional<String> oldImageName = postDao.findImageName(postId);
        String newImageName = saveFile(image);
        if (!postDao.updateImageName(postId, newImageName)) {
            imageStorage.delete(newImageName);
            throw new PostNotFoundException(postId);
        }
        oldImageName.ifPresent(imageStorage::delete);
    }

    @Override
    public PostImageContent getImage(long postId) {
        String imageName = postDao.findImageName(postId)
                .orElseThrow(() -> imageOrPostNotFound(postId));
        byte[] content = imageStorage.load(imageName)
                .orElseThrow(() -> new ImageNotFoundException(postId));
        return new PostImageContent(imageName, content);
    }

    private String saveFile(MultipartFile image) {
        try (InputStream content = image.getInputStream()) {
            return imageStorage.save(content, image.getOriginalFilename());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read uploaded image", e);
        }
    }

    private NotFoundException imageOrPostNotFound(long postId) {
        return postDao.existsById(postId)
                ? new ImageNotFoundException(postId)
                : new PostNotFoundException(postId);
    }
}
