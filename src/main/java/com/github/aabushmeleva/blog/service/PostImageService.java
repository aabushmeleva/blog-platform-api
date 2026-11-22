package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.dto.PostImageContent;
import org.springframework.web.multipart.MultipartFile;

public interface PostImageService {

    void updateImage(long postId, MultipartFile image);

    PostImageContent getImage(long postId);
}
