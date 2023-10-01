package com.springboot.blog.payload.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long Id;

    @NotBlank(message = "Comment body should not be blank")
    @Size(max=100, message = "Comment body should be below 100 characters!")
    private String body;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updatedDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userFullName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long postId;

    public void setId(Long id) {
        Id = id;
    }

    public void setBody(String body) {
        if(body!=null) this.body = body.trim();
        else this.body = body;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
