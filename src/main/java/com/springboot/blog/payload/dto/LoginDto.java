package com.springboot.blog.payload.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "email should not be blank")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Post Title should not be blank")
    private String password;
}
