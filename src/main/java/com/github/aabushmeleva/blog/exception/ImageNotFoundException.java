package com.github.aabushmeleva.blog.exception;

public class ImageNotFoundException extends NotFoundException {

    public ImageNotFoundException(long postId) {
        super("Image not found for post: " + postId);
    }
}
