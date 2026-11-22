package com.github.aabushmeleva.blog.storage;

import java.io.InputStream;
import java.util.Optional;

public interface ImageStorage {
    /* Saves the image and return image generated name */
    String save(InputStream content, String originalFilename);
    Optional<byte[]> load(String imageName);
    void delete(String imageName);
}
