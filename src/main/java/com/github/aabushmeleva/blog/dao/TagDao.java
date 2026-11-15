package com.github.aabushmeleva.blog.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TagDao {
    Map<Long, List<String>> findByPostIds(Collection<Long> postIds);

    /**
     * Replaces all tags of the post. Tags not in the collection are removed
     * Expects normalized distinct tags
     */
    void replaceTags(long postId, Collection<String> tags);
}
