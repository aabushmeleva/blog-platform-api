package com.github.aabushmeleva.blog.dao.rowmapper;

import com.github.aabushmeleva.blog.model.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PostRowMapper implements RowMapper<Post> {

    public static final PostRowMapper INSTANCE = new PostRowMapper();

    private PostRowMapper() {
    }

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Post(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("text"),
                rs.getLong("likes_count"),
                rs.getString("image_name")
        );
    }
}
