package com.github.aabushmeleva.blog.dao.jdbc;

import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.model.Post;
import com.github.aabushmeleva.blog.model.PostSearchQuery;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Repository
public class JdbcPostDao implements PostDao {

    private static final String INSERT = """
            INSERT INTO post (title, text, likes_count, image_name)
            VALUES (:title, :text, :likesCount, :imageName)
            """;

    private static final String[] ID_COLUMN = {"id"};

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcPostDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post save(Post post) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", post.title())
                .addValue("text", post.text())
                .addValue("likesCount", post.likesCount())
                .addValue("imageName", post.imageName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT, params, keyHolder, ID_COLUMN);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id == null) {
            throw new IllegalStateException("Database did not return generated id for post");
        }
        return new Post(id, post.title(), post.text(), post.likesCount(), post.imageName());
    }

    @Override
    public Optional<Post> findById(long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Post> findAll(PostSearchQuery query, int limit, long offset) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public long count(PostSearchQuery query) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean update(Post post) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean deleteById(long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public OptionalLong incrementLikes(long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean updateImageName(long id, String imageName) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<String> findImageName(long id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
