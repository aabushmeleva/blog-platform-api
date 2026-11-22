package com.github.aabushmeleva.blog.service.event;

/** Published after a post is deleted. ImageName is null if the post had no image */
public record PostDeletedEvent(long postId, String imageName) {}
