package com.github.aabushmeleva.blog.exception;

public class EmptyImageException extends InvalidRequestException {

    public EmptyImageException() {
        super("Image file must not be empty");
    }
}
