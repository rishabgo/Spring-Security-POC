package com.restapi.springrestapi;

import com.restapi.springrestapi.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringRestApiApplicationTests {

	@Autowired
	private PostRepository postRepository;
	@Test
	void contextLoads() {
	}

}
