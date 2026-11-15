package com.github.aabushmeleva.blog.model;

public record Post(
        Long id,
        String title,
        String text,
        long likesCount,
        String imageName
) {
    public Post withContent(String title, String text) {
        return new Post(id, title, text, likesCount, imageName);
    }
}
