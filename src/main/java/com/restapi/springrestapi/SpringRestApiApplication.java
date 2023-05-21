package com.restapi.springrestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class SpringRestApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestApiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /*@Autowired
    private PostRepository postRepository;*/

    @Override
    public void run(String... args) throws Exception {
/*
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i < 10000; i++) {
            Post post = Post.builder()
                    .postId((long) i)
                    .title("Post Title " + i)
                    .content("Post Content " + i)
                    .dateAdded(LocalDate.now())
                    .postCategory(PostCategory.SPORTS).build();
            posts.add(post);
        }

        postRepository.saveAll(posts);*/
    }
}
