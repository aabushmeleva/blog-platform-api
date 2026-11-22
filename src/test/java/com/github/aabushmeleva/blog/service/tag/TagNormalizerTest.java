package com.github.aabushmeleva.blog.service.tag;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagNormalizerTest {

    @Test
    void shouldNormalizeAndCleanFromEmptyAndDuplicateTag() {
        List<String> tags = List.of("# HorseshoeCrabs", " BIOETHICS ", "", "###Bioethics", "", " ");
        List<String> expected = List.of("bioethics", "horseshoecrabs");

        List<String> actual = TagNormalizer.normalize(tags);

        assertEquals(expected, actual);
    }
}
