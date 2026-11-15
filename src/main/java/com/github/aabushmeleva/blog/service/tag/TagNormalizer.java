package com.github.aabushmeleva.blog.service.tag;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public final class TagNormalizer {

    private static final String LEADING_HASHES = "^#+";

    private TagNormalizer() {
    }

    /**
     * Trims tags, removes leading '#', drops blanks, lowercases,
     * removes duplicates and sorts alphabetically
     */
    public static List<String> normalize(Collection<String> tags) {
        return tags.stream()
                .map(String::strip)
                .map(tag -> tag.replaceFirst(LEADING_HASHES, ""))
                .filter(tag -> !tag.isEmpty())
                .map(tag -> tag.toLowerCase(Locale.ROOT))
                .distinct()
                .sorted()
                .toList();
    }
}
