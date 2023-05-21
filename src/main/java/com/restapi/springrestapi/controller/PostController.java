package com.restapi.springrestapi.controller;

import com.restapi.springrestapi.dto.PostDto;
import com.restapi.springrestapi.entity.Post;
import com.restapi.springrestapi.service.PostService;
import com.restapi.springrestapi.util.AppConstants;
import com.restapi.springrestapi.util.PostPage;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody @NonNull final PostDto postDto) {

        final PostDto savedPost = postService.createPost(postDto);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {

        final PostDto postDto = postService.getPostById(postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostPage> getAllPosts(@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize,
                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDir) {

        final PostPage postPage = postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable @NonNull Long postId, @RequestBody @NonNull PostDto postDto) {

        final PostDto updatedPost = postService.updatePost(postId, postDto);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }
}
