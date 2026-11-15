package com.github.aabushmeleva.blog.dao.jdbc;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.dao.rowmapper.CommentRowMapper;
import com.github.aabushmeleva.blog.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcCommentDao implements CommentDao {

    private static final String INSERT_SQL = """
            INSERT INTO comment (post_id, text)
            VALUES (:postId, :text)
            """;

    private static final String FIND_BY_POST_ID_SQL = """
            SELECT id, post_id, text
            FROM comment
            WHERE post_id = :postId
            ORDER BY id
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, post_id, text
            FROM comment
            WHERE id = :id
            """;

    private static final String UPDATE_SQL = """
            UPDATE comment
            SET text = :text
                WHERE id = :id
            """;

    private static final String DELETE_BY_ID_AND_POST_ID_SQL = """
            DELETE FROM comment
            WHERE id = :id AND post_id = :postId
            """;

    private static final String COUNT_BY_POST_IDS_SQL = """
            SELECT post_id, COUNT(*) AS comments_count
            FROM comment
            WHERE post_id IN (:postIds)
            GROUP BY post_id
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Comment> findByPostId(long postId) {
        return jdbcTemplate.query(FIND_BY_POST_ID_SQL, Map.of("postId", postId), CommentRowMapper.INSTANCE);
    }

    @Override
    public Optional<Comment> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, Map.of("id", id), CommentRowMapper.INSTANCE)
                .stream()
                .findFirst();
    }

    @Override
    public Comment save(Comment comment) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postId", comment.postId())
                .addValue("text", comment.text());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SQL, params, keyHolder, new String[]{"id"});
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Comment(id, comment.postId(), comment.text());
    }

    @Override
    public boolean update(Comment comment) {
        var params = new MapSqlParameterSource()
                .addValue("id", comment.id())
                .addValue("text", comment.text());
        return jdbcTemplate.update(UPDATE_SQL, params) > 0;
    }

    @Override
    public boolean deleteByIdAndPostId(long id, long postId) {
        var params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("postId", postId);
        return jdbcTemplate.update(DELETE_BY_ID_AND_POST_ID_SQL, params) > 0;
    }

    @Override
    public Map<Long, Long> countByPostIds(Collection<Long> postIds) {
        if (postIds.isEmpty()) {
            return Map.of();
        }
        Map<Long, Long> counts = new HashMap<>();
        jdbcTemplate.query(COUNT_BY_POST_IDS_SQL, Map.of("postIds", postIds), rs -> {
            counts.put(rs.getLong("post_id"), rs.getLong("comments_count"));
        });
        return counts;
    }
}
