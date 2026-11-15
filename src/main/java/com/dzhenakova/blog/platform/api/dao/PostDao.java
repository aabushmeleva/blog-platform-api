package com.dzhenakova.blog.platform.api.dao;

import com.dzhenakova.blog.platform.api.model.Post;
import com.dzhenakova.blog.platform.api.model.PostSearchQuery;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public interface PostDao {
    List<Post> findAll(PostSearchQuery query, int limit, long offset);
    long count(PostSearchQuery query);
    Optional<Post> findById(long id);
    Post save(Post post);
    boolean update(Post post);
    boolean deleteById(long id);
    OptionalLong incrementLikes(long id);
    boolean updateImageName(long id, String imageName);
    Optional<String> findImageName(long id);
}
