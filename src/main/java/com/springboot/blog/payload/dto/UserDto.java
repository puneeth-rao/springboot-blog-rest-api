package com.springboot.blog.payload.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.blog.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Name should not be blank")
    @Size(min=3, message = "Name should be greater than 3 characters wide!")
    private String fullName;

    @Email(message = "Email Id is not valid")
    private String email;

    @NotBlank(message = "Password should not be null/empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$", message = "Password must be greater than 6 characters and mustcontain at least one digit, at least one upper case alphabet, at least one lower case alphabet, at least one special character which includes !@#$%&*()-+=^ and doesn’t contain any white space.")
    @Size(min=5, max=16, message = "Password must be between 6 to 16 characters wide!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Role> roles;
}
