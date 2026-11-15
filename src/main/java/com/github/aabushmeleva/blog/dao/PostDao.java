package com.github.aabushmeleva.blog.dao;

import com.github.aabushmeleva.blog.model.Post;
import com.github.aabushmeleva.blog.model.PostSearchQuery;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public interface PostDao {
    Post save(Post post);

    Optional<Post> findById(long id);

    boolean existsById(long id);

    List<Post> findAll(PostSearchQuery query, int limit, long offset);

    long count(PostSearchQuery query);

    boolean update(Post post);

    boolean deleteById(long id);

    OptionalLong incrementLikes(long id);

    boolean updateImageName(long id, String imageName);

    Optional<String> findImageName(long id);
}
