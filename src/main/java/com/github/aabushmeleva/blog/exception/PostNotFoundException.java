package com.github.aabushmeleva.blog.exception;

public class PostNotFoundException extends NotFoundException {

    public PostNotFoundException(long postId) {
        super("Post not found: " + postId);
    }
}
