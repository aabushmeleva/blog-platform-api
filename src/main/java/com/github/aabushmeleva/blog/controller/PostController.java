package com.github.aabushmeleva.blog.controller;

import com.github.aabushmeleva.blog.dto.PostCreateRequest;
import com.github.aabushmeleva.blog.dto.PostPageResponse;
import com.github.aabushmeleva.blog.dto.PostResponse;
import com.github.aabushmeleva.blog.dto.PostUpdateRequest;
import com.github.aabushmeleva.blog.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody PostCreateRequest request) {
        return postService.create(request);
    }

    @GetMapping("/{id}")
    public PostResponse findById(@PathVariable long id) {
        return postService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        postService.delete(id);
    }

    @PutMapping("/{id}")
    public PostResponse update(@PathVariable long id,
                               @Valid @RequestBody PostUpdateRequest request) {
        return postService.update(id, request);
    }

    @PostMapping("/{id}/likes")
    public long incrementLikes(@PathVariable long id) {
        return postService.incrementLikes(id);
    }

    @GetMapping
    public PostPageResponse findPage(@RequestParam String search,
                                     @RequestParam @Min(1) int pageNumber,
                                     @RequestParam @Min(1) @Max(100) int pageSize) {
        return postService.findPage(search, pageNumber, pageSize);
    }
}
