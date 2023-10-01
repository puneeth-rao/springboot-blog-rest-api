package com.springboot.blog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class BlogRestApiApplicationTests {

	@Test
	void contextLoads() {
		String encodedPassword = passwordEncoder().encode("P@ssword");
		System.out.println("encodedPassword = "+encodedPassword);
	}

	public static PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
