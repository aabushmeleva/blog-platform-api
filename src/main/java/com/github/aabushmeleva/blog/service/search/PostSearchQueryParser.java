package com.github.aabushmeleva.blog.service.search;

import com.github.aabushmeleva.blog.model.PostSearchQuery;
import com.github.aabushmeleva.blog.service.tag.TagNormalizer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class PostSearchQueryParser {

    private static final String TAG_PREFIX = "#";

    private PostSearchQueryParser() {
    }

    public static PostSearchQuery parse(String search) {
        if (search == null || search.isBlank()) {
            return new PostSearchQuery("", Set.of());
        }
        Map<Boolean, List<String>> words = Arrays.stream(search.strip().split("\\s+"))
                .collect(Collectors.partitioningBy(word -> word.startsWith(TAG_PREFIX)));
        String title = String.join(" ", words.get(false));
        Set<String> tags = Set.copyOf(TagNormalizer.normalize(words.get(true)));
        return new PostSearchQuery(title, tags);
    }
}
