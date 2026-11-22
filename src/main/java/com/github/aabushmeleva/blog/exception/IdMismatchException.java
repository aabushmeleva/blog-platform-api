package com.github.aabushmeleva.blog.exception;

public class IdMismatchException extends InvalidRequestException {

    public IdMismatchException(String name, long pathId, long bodyId) {
        super("Path %s %d does not match body %s %d".formatted(name, pathId, name, bodyId));
    }
}
