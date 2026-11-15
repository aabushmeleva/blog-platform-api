package com.github.aabushmeleva.blog.service.tag;

import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.exception.PostNotFoundException;
import com.github.aabushmeleva.blog.service.PostImageService;
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
        Optional<String> oldImageName = postDao.findImageName(postId);
        String newImageName = saveFile(image);
        if (!postDao.updateImageName(postId, newImageName)) {
            imageStorage.delete(newImageName);
            throw new PostNotFoundException(postId);
        }
        oldImageName.ifPresent(imageStorage::delete);
    }

    @Override
    public byte[] getImage(long postId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private String saveFile(MultipartFile image) {
        try (InputStream content = image.getInputStream()) {
            return imageStorage.save(content, image.getOriginalFilename());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read uploaded image", e);
        }
    }
}
