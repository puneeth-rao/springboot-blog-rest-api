package com.springboot.blog.exception;

import com.springboot.blog.payload.ExceptionResponse;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex, WebRequest webRequest){
        ExceptionResponse errResponse = new ExceptionResponse();
        errResponse.setTimeStamp(new Date());
        errResponse.setStatusCode(String.valueOf(HttpStatus.NOT_FOUND));
        errResponse.setMessage(ex.getMessage());
        errResponse.setPath(webRequest.getDescription(false));

        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errResponse, HttpStatus.NOT_FOUND);
    }

    //to handle query param string mismatch with dto object
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ExceptionResponse> propertyReferenceExceptionHandler(PropertyReferenceException ex, WebRequest webRequest){
        ExceptionResponse errResponse = new ExceptionResponse();
        errResponse.setTimeStamp(new Date());
        errResponse.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST));
        errResponse.setMessage(ex.getMessage());
        errResponse.setPath(webRequest.getDescription(false));

        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }

    //duplicate entry to DB
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex, WebRequest webRequest){
        ExceptionResponse errResponse = new ExceptionResponse();
        errResponse.setTimeStamp(new Date());
        errResponse.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST));
        errResponse.setMessage(ex.getMessage());
        errResponse.setPath(webRequest.getDescription(false));

        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }

    //Generalised exception
    @ExceptionHandler(BlogApiException.class)
    public ResponseEntity<ExceptionResponse> blogApiExceptionHandler(BlogApiException ex, WebRequest webRequest){
        ExceptionResponse errResponse = new ExceptionResponse();
        errResponse.setTimeStamp(new Date());
        errResponse.setStatusCode(String.valueOf(ex.getStatus()));
        errResponse.setMessage(ex.getMessage());
        errResponse.setPath(webRequest.getDescription(false));

        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errResponse, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> dataValidationExceptionHandler(MethodArgumentNotValidException ex){
        Map<String, String> errResponse = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errResponse.put(fieldName, message);
        });
        errResponse.put("status", ex.getStatusCode().toString());

        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedExceptionHandler(AccessDeniedException ex, WebRequest webRequest){
        ExceptionResponse errResponse = new ExceptionResponse();
        errResponse.setTimeStamp(new Date());
        errResponse.setStatusCode(String.valueOf(HttpStatus.FORBIDDEN));
        errResponse.setMessage(ex.getMessage());
        errResponse.setPath(webRequest.getDescription(false));

        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> generalExceptionHandler(Exception ex, WebRequest webRequest){
        ExceptionResponse errResponse = new ExceptionResponse();
        errResponse.setTimeStamp(new Date());
        errResponse.setStatusCode("500");
        errResponse.setMessage(ex.getMessage());
        errResponse.setPath(webRequest.getDescription(false));

        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
