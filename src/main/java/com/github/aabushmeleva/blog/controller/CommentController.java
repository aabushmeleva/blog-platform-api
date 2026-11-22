package com.github.aabushmeleva.blog.controller;

import com.github.aabushmeleva.blog.dto.CommentCreateRequest;
import com.github.aabushmeleva.blog.dto.CommentResponse;
import com.github.aabushmeleva.blog.dto.CommentUpdateRequest;
import com.github.aabushmeleva.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@PathVariable long postId,
                                  @Valid @RequestBody CommentCreateRequest request) {
        RequestIds.requireMatch("postId", postId, request.postId());
        return commentService.create(postId, request);
    }

    @GetMapping
    public List<CommentResponse> findByPostId(@PathVariable long postId) {
        return commentService.findByPostId(postId);
    }

    @GetMapping("/{commentId}")
    public CommentResponse findById(@PathVariable long postId, @PathVariable long commentId) {
        return commentService.findById(postId, commentId);
    }

    @PutMapping("/{commentId}")
    public CommentResponse update(@PathVariable long commentId,
                                  @Valid @RequestBody CommentUpdateRequest request) {
        // By commentId only due the front bug
        RequestIds.requireMatch("id", commentId, request.id());
        return commentService.update(commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable long postId, @PathVariable long commentId) {
        commentService.delete(postId, commentId);
    }
}
