package com.github.aabushmeleva.blog.exception;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException(long commentId) {
        super("Comment not found: " + commentId);
    }
}
