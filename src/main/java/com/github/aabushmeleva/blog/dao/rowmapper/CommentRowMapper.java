package com.github.aabushmeleva.blog.dao.rowmapper;

import com.github.aabushmeleva.blog.model.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CommentRowMapper implements RowMapper<Comment> {

    public static final CommentRowMapper INSTANCE = new CommentRowMapper();

    private CommentRowMapper() {
    }

    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(rs.getLong("id"), rs.getLong("post_id"), rs.getString("text"));
    }
}
