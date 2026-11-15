package com.github.aabushmeleva.blog.controller;

import com.github.aabushmeleva.blog.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts/{postId}/image")
@RequiredArgsConstructor
public class PostImageController {

    private final PostImageService postImageService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateImage(@PathVariable long postId, @RequestParam("image") MultipartFile image) {
        postImageService.updateImage(postId, image);
    }
}
