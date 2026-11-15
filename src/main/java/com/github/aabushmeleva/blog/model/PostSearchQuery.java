package com.github.aabushmeleva.blog.model;

import java.util.Set;

public record PostSearchQuery(String titleSubstring, Set<String> tags) {
}
