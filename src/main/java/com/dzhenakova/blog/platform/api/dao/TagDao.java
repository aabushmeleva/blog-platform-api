package com.dzhenakova.blog.platform.api.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TagDao {
    Map<Long, List<String>> findByPostIds(Collection<Long> postIds);
    void replaceTags(long postId, Collection<String> tags);
}
