package com.github.aabushmeleva.blog.dao.jdbc;

import com.github.aabushmeleva.blog.dao.TagDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcTagDao implements TagDao {

    private static final String FIND_BY_POST_IDS_SQL = """
            SELECT post_id, name
            FROM post_tag
            WHERE post_id IN (:postIds)
            ORDER BY post_id, name
            """;

    private static final String DELETE_BY_POST_ID_SQL = """
            DELETE FROM post_tag
            WHERE post_id = :postId
            """;

    private static final String INSERT_SQL = """
            INSERT INTO post_tag (post_id, name)
            VALUES (:postId, :name)
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Map<Long, List<String>> findByPostIds(Collection<Long> postIds) {
        if (postIds.isEmpty()) {
            return Map.of();
        }
        Map<Long, List<String>> tagsByPostId = new HashMap<>();
        jdbcTemplate.query(FIND_BY_POST_IDS_SQL, Map.of("postIds", postIds), rs -> {
            tagsByPostId.computeIfAbsent(rs.getLong("post_id"), id -> new ArrayList<>())
                    .add(rs.getString("name"));
        });
        return tagsByPostId;
    }

    @Override
    public void replaceTags(long postId, Collection<String> tags) {
        jdbcTemplate.update(DELETE_BY_POST_ID_SQL, Map.of("postId", postId));
        SqlParameterSource[] batch = tags.stream()
                .map(tag -> new MapSqlParameterSource()
                        .addValue("postId", postId)
                        .addValue("name", tag))
                .toArray(SqlParameterSource[]::new);
        jdbcTemplate.batchUpdate(INSERT_SQL, batch);
    }
}
