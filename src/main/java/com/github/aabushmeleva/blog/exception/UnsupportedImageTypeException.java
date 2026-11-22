package com.github.aabushmeleva.blog.exception;

import java.util.List;

public class UnsupportedImageTypeException extends InvalidRequestException {

    public UnsupportedImageTypeException(List<String> allowedExtensions) {
        super("Image file must have one of the extensions: " + String.join(", ", allowedExtensions));
    }
}
