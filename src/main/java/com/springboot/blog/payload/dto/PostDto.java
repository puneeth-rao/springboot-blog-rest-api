package com.springboot.blog.payload.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long Id;

    @NotBlank(message = "Post Title should not be blank")
    @Size(min=5, message = "Post Title should be greater than 5 characters wide!")
    private String title;

    @NotBlank(message = "Post description should not be blank")
    @Size(min=10, max=100, message = "Post description should be between 10 to 100 characters!")
    private String description;

    @NotBlank(message = "Post content should not be blank")
    @Size(min=10, max=1000, message = "Post content should be between 10 to 1000 characters!")
    private String content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updatedDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userFullName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<CommentDto> comments;

    @NotNull(message = "Enter a category Id for the post.")
    private Long categoryId;


    //wrote setters manually to trim white spaces
    public void setId(Long id) {
        Id = id;
    }

    public void setTitle(String title) {
        if(title!=null) this.title = title.trim();
        else this.title = title;
    }

    public void setDescription(String description) {
        if(description!=null) this.description = description.trim();
        else this.description = description;
    }

    public void setContent(String content) {
        if(content!=null) this.content = content.trim();
        else this.content = content;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setComments(Set<CommentDto> comments) {
        this.comments = comments;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}
