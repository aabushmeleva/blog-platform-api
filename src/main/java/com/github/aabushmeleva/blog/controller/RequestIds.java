package com.github.aabushmeleva.blog.controller;

import com.github.aabushmeleva.blog.exception.IdMismatchException;

final class RequestIds {

    private RequestIds() {
    }

    static void requireMatch(String name, long pathId, Long bodyId) {
        if (bodyId != null && bodyId != pathId) {
            throw new IdMismatchException(name, pathId, bodyId);
        }
    }
}
