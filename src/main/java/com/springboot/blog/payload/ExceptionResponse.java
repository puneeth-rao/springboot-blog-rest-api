package com.springboot.blog.payload;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private Date timeStamp;
    private String statusCode;
    private String message;
    private String path;
}
