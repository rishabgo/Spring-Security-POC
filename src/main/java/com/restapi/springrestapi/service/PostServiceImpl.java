package com.restapi.springrestapi.service;

import com.restapi.springrestapi.dto.PostDto;
import com.restapi.springrestapi.entity.Post;
import com.restapi.springrestapi.exception.PostNotFoundException;
import com.restapi.springrestapi.repository.PostRepository;
import com.restapi.springrestapi.util.PostPage;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = {"Posts"})
public class PostServiceImpl implements PostService {

    public static final String EXCEPTION_MESSAGE = "Post with given id doesn't exist";
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;


    public PostServiceImpl(ModelMapper modelMapper, PostRepository postRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
    }

    public PostDto createPost(final PostDto postDto) {

        Post post = modelMapper.map(postDto, Post.class);
        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }

    @Cacheable(key = "#postId")
    public PostDto getPostById(Long postId) {
        logger.info("Getting post for id {}", postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(EXCEPTION_MESSAGE));
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostPage getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortBy.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> page = postRepository.findAll(pageRequest);

        List<PostDto> postDtoList = page.getContent().stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        return PostPage.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .posts(postDtoList)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }

    @CacheEvict(key = "#postId")
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(EXCEPTION_MESSAGE));
        postRepository.delete(post);
    }

    @CachePut(key = "#postId")
    public PostDto updatePost(Long postId, PostDto postDto) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(EXCEPTION_MESSAGE));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDateAdded(postDto.getDateAdded());

        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

}
