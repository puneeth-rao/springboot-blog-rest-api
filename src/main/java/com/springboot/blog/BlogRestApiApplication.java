package com.springboot.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot-3 Blog Rest API's",
				description = "This is a Blog app with user signup/login with role-based authentication using JWT, CRUD operations with posts, category, comments",
				version = "v1.0",
				contact = @Contact(
						name = "Puneeth Rao",
						email = "puneethknrao@gmail.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Blog App Documentation",
				url = "https://github.com/puneeth-rao/springboot-blog-rest-api"
		)
)
public class BlogRestApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogRestApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
