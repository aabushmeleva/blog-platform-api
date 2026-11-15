package com.github.aabushmeleva.blog.dao.jdbc;

import com.github.aabushmeleva.blog.dao.CommentDao;
import com.github.aabushmeleva.blog.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Comment> findByPostId(long postId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<Comment> findById(long id) {
        throw new UnsupportedOperationException("Not implemented");
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
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean deleteById(long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map<Long, Long> countByPostIds(Collection<Long> postIds) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
