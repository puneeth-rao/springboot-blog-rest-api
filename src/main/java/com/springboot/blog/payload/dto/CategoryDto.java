package com.springboot.blog.payload.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.blog.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Category should not be blank")
    @Size(min=3, message = "Category should be greater than 3 characters wide!")
    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        if(name!=null) this.name = name.trim();
        else this.name = name;
    }

}
