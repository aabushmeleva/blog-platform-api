package com.github.aabushmeleva.blog.storage;

import java.io.InputStream;

public interface ImageStorage {
    /* Saves the image and return image generated name */
    String save(InputStream content, String originalFilename);
    byte[] load(String imageName);
    void delete(String imageName);
}
