package com.restapi.springrestapi.service;

import com.restapi.springrestapi.dto.PostDto;
import com.restapi.springrestapi.util.PostPage;

public interface PostService {

    PostDto createPost(final PostDto postDto);

    PostDto getPostById(Long postId);

    void deletePost(Long postId);

    PostDto updatePost(Long postId, PostDto postDto);

    PostPage getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir);
}
