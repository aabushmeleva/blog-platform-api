package com.github.aabushmeleva.blog.model;

import java.util.Set;

public record PostSearchQuery(String titleSubstring, Set<String> tags) {

    public PostSearchQuery {
        titleSubstring = titleSubstring == null ? "" : titleSubstring;
        tags = tags == null ? Set.of() : Set.copyOf(tags);
    }

    public boolean hasTitle() {
        return !titleSubstring.isEmpty();
    }

    public boolean hasTags() {
        return !tags.isEmpty();
    }
}

