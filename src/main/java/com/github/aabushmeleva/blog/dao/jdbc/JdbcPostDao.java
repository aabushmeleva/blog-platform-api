package com.github.aabushmeleva.blog.dao.jdbc;

import com.github.aabushmeleva.blog.dao.PostDao;
import com.github.aabushmeleva.blog.dao.rowmapper.PostRowMapper;
import com.github.aabushmeleva.blog.model.Post;
import com.github.aabushmeleva.blog.model.PostSearchQuery;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@Repository
public class JdbcPostDao implements PostDao {

    private static final String INSERT_SQL = """
            INSERT INTO post (title, text, likes_count, image_name)
            VALUES (:title, :text, :likesCount, :imageName)
            """;


    private static final String SELECT_BY_ID_SQL = """
            SELECT id, title, text, likes_count, image_name
            FROM post
            WHERE id = :id
            """;

    private static final String EXISTS_BY_ID_SQL = """
            SELECT EXISTS (SELECT 1 FROM post WHERE id = :id)
            """;

    private static final String DELETE_BY_ID_SQL = "DELETE FROM post WHERE id = :id";

    static final String UPDATE_SQL = """
            UPDATE post
            SET title = :title, text = :text
            WHERE id = :id
            """;

    private static final String INCREMENT_LIKES_SQL = """
            UPDATE post
            SET likes_count = likes_count + 1
            WHERE id = :id
            """;

    private static final String FIND_LIKES_COUNT_SQL = """
            SELECT likes_count
            FROM post
            WHERE id = :id
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
        jdbcTemplate.update(INSERT_SQL, params, keyHolder, ID_COLUMN);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id == null) {
            throw new IllegalStateException("Database did not return generated id for post");
        }
        return new Post(id, post.title(), post.text(), post.likesCount(), post.imageName());
    }

    @Override
    public Optional<Post> findById(long id) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL, new MapSqlParameterSource("id", id), PostRowMapper.INSTANCE)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsById(long id) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(EXISTS_BY_ID_SQL, Map.of("id", id), Boolean.class));
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
        var params = new MapSqlParameterSource()
                .addValue("id", post.id())
                .addValue("title", post.title())
                .addValue("text", post.text());
        return jdbcTemplate.update(UPDATE_SQL, params) > 0;
    }

    @Override
    public boolean deleteById(long id) {
        int deletedRows = jdbcTemplate.update(DELETE_BY_ID_SQL, Map.of("id", id));
        return deletedRows > 0;
    }

    @Override
    public OptionalLong incrementLikes(long id) {
        Map<String, Object> params = Map.of("id", id);
        if (jdbcTemplate.update(INCREMENT_LIKES_SQL, params) == 0) {
            return OptionalLong.empty();
        }
        return OptionalLong.of(jdbcTemplate.queryForObject(FIND_LIKES_COUNT_SQL, params, Long.class));
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
