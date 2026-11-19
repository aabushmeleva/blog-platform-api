package com.github.aabushmeleva.blog.exception;

public class InvalidTagException extends InvalidRequestException {

    public InvalidTagException(int maxLength) {
        super("Tag must not be longer than %d characters".formatted(maxLength));
    }
}
