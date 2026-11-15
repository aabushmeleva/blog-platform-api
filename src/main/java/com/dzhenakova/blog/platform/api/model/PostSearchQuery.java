package com.dzhenakova.blog.platform.api.model;

import java.util.Set;

public record PostSearchQuery(String titleSubstring, Set<String> tags) {
}
