package com.restapi.springrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restapi.springrestapi.dto.PostDto;
import com.restapi.springrestapi.entity.Post;
import com.restapi.springrestapi.entity.PostCategory;
import com.restapi.springrestapi.exception.PostNotFoundException;
import com.restapi.springrestapi.service.PostServiceImpl;
import com.restapi.springrestapi.util.PostPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@WithMockUser(username = "rishabh", password = "test", roles = "ADMIN")
class PostControllerTest {

    private static final String BASE_URL = "/api/v1";

    @MockBean
    private PostServiceImpl postService;

    @Autowired
    protected MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static PostDto postDto;

    @BeforeAll
    static void beforeAll() {
        postDto = createPostDto();
    }

    @Test
    void testCreatePost() throws Exception {

        PostDto post = createPostDto();
        String jsonRequest = objectMapper.writeValueAsString(post);
        when(postService.createPost(any(PostDto.class))).thenReturn(post);

        mockMvc.perform(post(BASE_URL.concat("/posts"))
                        .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Java11")))
                .andExpect(jsonPath("$.content", is("new features in Java11")));
    }

    @Test
    void testGetPostById() throws Exception {

        when(postService.getPostById(any(Long.class))).thenReturn(createPostDto());

        mockMvc.perform(get(BASE_URL.concat("/posts/1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Java11")))
                .andExpect(jsonPath("$.content", is("new features in Java11")));

    }

    @Test
    void testGetPostByIdWhenPostIdIsInvalid() throws Exception {
        when(postService.getPostById(any(Long.class))).thenThrow(new PostNotFoundException("Post with given id doesn't exist"));

        ResultActions response = mockMvc.perform(get(BASE_URL.concat("/posts/2")));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetAllPost() throws Exception {

        PostDto post1 = PostDto.builder()
                .title("java17 revised")
                .content("new features in java17")
                .dateAdded(LocalDate.now().minusDays(2))
                .build();

        PostPage postPage = createPostPage(post1);

        when(postService.getAllPosts(anyInt(), anyInt(), anyString(), anyString())).thenReturn(postPage);

        mockMvc.perform(get(BASE_URL.concat("/posts")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.pageNumber", is(0)));
    }

    private static PostPage createPostPage(PostDto post1) {
        return PostPage.builder()
                .posts(List.of(post1, postDto))
                .totalElements(2)
                .pageNumber(0)
                .lastPage(Boolean.TRUE)
                .build();
    }

    @Test
    void testDeletePost() throws Exception {

        Mockito.doNothing().when(postService).deletePost(any(Long.class));

        mockMvc.perform(delete(BASE_URL.concat("/posts/1")))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdatePostWhenPostIdIsInvalid() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(postDto);

        when(postService.updatePost(any(Long.class), any(PostDto.class))).thenThrow(new PostNotFoundException("Post with given id doesn't exist"));

        ResultActions response = mockMvc.perform(put(BASE_URL.concat("/posts/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdatePost() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(postDto);

        when(postService.updatePost(any(Long.class), any(PostDto.class)))
                .thenReturn(postDto);

        mockMvc.perform(put(BASE_URL.concat("/posts/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Java11")))
                .andExpect(jsonPath("$.content", is("new features in Java11")));
    }

    private static PostDto createPostDto() {

        return PostDto.builder().title("Java11")
                .content("new features in Java11")
                .dateAdded(LocalDate.now())
                .postCategory(PostCategory.EDUCATIONAL)
                .build();

    }

    private Post createPost() {
        return Post.builder()
                .postId(1L)
                .title("Java11")
                .content("new features in Java11")
                .dateAdded(LocalDate.now())
                .postCategory(PostCategory.EDUCATIONAL)
                .build();
    }

}