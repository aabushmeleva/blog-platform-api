package com.github.aabushmeleva.blog.storage;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class ImageExtensions {

    public static final List<String> ALLOWED = List.of("jpg", "jpeg", "png", "gif", "webp");

    private ImageExtensions() {
    }

    public static Optional<String> of(String filename) {
        String extension = StringUtils.getFilenameExtension(filename);
        if (extension == null) {
            return Optional.empty();
        }
        String normalized = extension.toLowerCase(Locale.ROOT);
        return ALLOWED.contains(normalized) ? Optional.of(normalized) : Optional.empty();
    }
}
