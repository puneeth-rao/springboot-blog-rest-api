package com.springboot.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.payload.ExceptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

//handles exception when unauthorised user try to use endpoints that requires authentication
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        //previous code for all jwt exceptions no response was getting
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;
        // Check if the request as any exception that we have stored in Request
        final Exception exception = (Exception) request.getAttribute("exception");

        // If yes then use it to create the response message else use the authException
        if (exception != null) {
            ExceptionResponse errResponse = new ExceptionResponse();
            errResponse.setTimeStamp(new Date());
            errResponse.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST));
            errResponse.setMessage(exception.getMessage());
            byte[] body = new ObjectMapper().writeValueAsBytes(errResponse);
            response.getOutputStream().write(body);
        } else {
            if (authException.getCause() != null) {
                message = authException.getCause().toString() + " " + authException.getMessage();
            } else {
                message = authException.getMessage();
            }

            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));

            response.getOutputStream().write(body);
        }
    }
}
