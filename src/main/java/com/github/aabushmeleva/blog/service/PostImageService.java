package com.github.aabushmeleva.blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostImageService {

    void updateImage(long postId, MultipartFile image);

    byte[] getImage(long postId);
}
