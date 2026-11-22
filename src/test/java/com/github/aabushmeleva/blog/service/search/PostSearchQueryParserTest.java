package com.github.aabushmeleva.blog.service.search;

import com.github.aabushmeleva.blog.model.PostSearchQuery;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostSearchQueryParserTest {

    @Test
    void shouldParseTitleAndNormalizeTags() {
        String search = "  HorseshoeCrabs   #BIOETHICS  pharmacology  #LALtest  #bioethics  ";
        String expectedTitle = "HorseshoeCrabs pharmacology";
        Set<String> expectedTags = Set.of("bioethics", "laltest");

        PostSearchQuery actual = PostSearchQueryParser.parse(search);

        assertEquals(expectedTitle, actual.titleSubstring());
        assertEquals(expectedTags, actual.tags());
    }
}
