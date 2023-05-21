package com.restapi.springrestapi.service;

import com.restapi.springrestapi.dto.PostDto;
import com.restapi.springrestapi.entity.Post;
import com.restapi.springrestapi.entity.PostCategory;
import com.restapi.springrestapi.exception.PostNotFoundException;
import com.restapi.springrestapi.repository.PostRepository;
import com.restapi.springrestapi.util.PostPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = createPostEntity();
    }

    @Test
    void givenPostObject_whenSavePost_thenReturnPostObject() {

        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto postDto = createPostDto();
        PostDto savedPost = postService.createPost(postDto);

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getPostId()).isEqualTo(1);
    }

    @Test
    void givenPostPage_whenGetAllPost_thenReturnPostPage() {

        Post post1 = Post.builder()
                .postId(2L)
                .title("java8 revised")
                .content("new features in java8")
                .dateAdded(LocalDate.now())
                .build();
        Page<Post> page = new PageImpl<>(List.of(post, post1));

        when(postRepository.findAll(any(Pageable.class))).thenReturn(page);

        PostPage posts = postService.getAllPosts(0, 5, "postId", "asc");

        assertThat(posts.getPosts().size()).isEqualTo(2);
        assertThat(posts.getPosts().get(1).getTitle()).isEqualTo("java8 revised");
        assertThat(posts.getPosts().get(1).getContent()).isEqualTo("new features in java8");
    }

    @Test
    void givenPostId_whenGetPostById_thenReturnPostObject() {

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

        PostDto postDto = postService.getPostById(1L);

        assertThat(postDto.getTitle()).isEqualTo("Java11");
    }

    @Test
    void givenInvalidPostId_whenGetPostById_thenThrowException() {

        when(postRepository.findById(any(Long.class))).thenThrow(new PostNotFoundException("Post with given id doesn't exist"));
        PostNotFoundException postNotFoundException = assertThrows(PostNotFoundException.class, () ->
                postService.getPostById(1L));

        assertThat(postNotFoundException.getMessage()).isEqualTo("Post with given id doesn't exist");
    }

    @Test
    void givenPostObject_whenUpdatePost_thenReturnUpdatedPost() {

        PostDto postDto = createPostDto();
        postDto.setTitle("Java 17");
        postDto.setContent("new features in java17");

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        PostDto updatedPost = postService.updatePost(1L, postDto);

        assertThat(updatedPost.getTitle()).isEqualTo("Java 17");
        assertThat(updatedPost.getContent()).isEqualTo("new features in java17");
    }

    @Test
    void givenPostId_whenDeletePost_thenNothing() {

        Long postId = 1L;

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        willDoNothing().given(postRepository).delete(any(Post.class));

        postService.deletePost(postId);

        Mockito.verify(postRepository, times(1)).delete(any(Post.class));
    }

    private Post createPostEntity() {
        return Post.builder()
                .postId(1L)
                .title("Java11")
                .content("new features in Java11")
                .dateAdded(LocalDate.now())
                .postCategory(PostCategory.EDUCATIONAL)
                .build();
    }

    private static PostDto createPostDto() {

        return PostDto.builder().title("Java11")
                .content("new features in Java11")
                .dateAdded(LocalDate.now())
                .postCategory(PostCategory.EDUCATIONAL)
                .build();
    }
}