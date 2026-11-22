package com.github.aabushmeleva.blog.exception;

public class InvalidRequestException extends RuntimeException{

    protected InvalidRequestException(String message) {
        super(message);
    }
}
