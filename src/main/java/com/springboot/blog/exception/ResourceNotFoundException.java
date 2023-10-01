package com.springboot.blog.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private Long fieldValue;
    private String strFieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValue){
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String strFieldValue){
        super(String.format("%s not found with %s : %s", resourceName, fieldName, strFieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.strFieldValue = strFieldValue;
    }
}
