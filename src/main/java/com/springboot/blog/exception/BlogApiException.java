package com.springboot.blog.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BlogApiException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public BlogApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
