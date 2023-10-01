package com.springboot.blog.controller;

import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.CommentDto;
import com.springboot.blog.payload.dto.PostDto;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
//below annotation is for swagger-ui
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Post Api's")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //get all posts
    @Operation(
            summary = "Get all posts",
            description = "This API is used to get all posts, user can also mention the page number(default=0), page size(default=10), sort by(default=createdDate), and sort direction(ASC or DESC)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping
    public ResponseEntity<PageResponse> getAllPosts(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUM_VALUE, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE_VALUE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY_VALUE, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR_VALUE, required = false) String sortDir

    ){
        return new ResponseEntity<>(this.postService.getAllPosts(pageNum, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //get post by ID
    @Operation(
            summary = "Get a post by postId",
            description = "This API is used to get a a post by postId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(
            @PathVariable("id") Long postId
    ){
        return new ResponseEntity<>(this.postService.getPostById(postId), HttpStatus.OK);
    }

    //get all posts by category
    @Operation(
            summary = "Get all posts by categoryId",
            description = "This API is used to get all posts by categoryId, user can also mention the page number(default=0), page size(default=10), sort by(default=createdDate), and sort direction(ASC or DESC)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse> getAllPostsByCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUM_VALUE, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE_VALUE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY_VALUE, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR_VALUE, required = false) String sortDir

    ){
        return new ResponseEntity<>(this.postService.getAllPostsByCategory(categoryId, pageNum, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //get all posts by user
    @Operation(
            summary = "Get all posts by userId",
            description = "This API is used to get all posts by userId, user can also mention the page number(default=0), page size(default=10), sort by(default=createdDate), and sort direction(ASC or DESC)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse> getAllPostsByUser(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUM_VALUE, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE_VALUE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY_VALUE, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR_VALUE, required = false) String sortDir

    ){
        return new ResponseEntity<>(this.postService.getAllPostsByUser(userId, pageNum, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //search Posts by keyword
    @Operation(
            summary = "Search for posts by title",
            description = "This API is used to search all posts by title, user can also mention the page number(default=0), page size(default=10)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/search")
    public ResponseEntity<PageResponse> searchPostByKeyword(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUM_VALUE, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE_VALUE, required = false) Integer pageSize
    ){
        return new ResponseEntity<>(this.postService.searchPosts(keyword, pageNum, pageSize), HttpStatus.OK);
    }

    //create Post
    @Operation(
            summary = "Create post",
            description = "This API is used to create post"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED",
            content = @Content(schema = @Schema(implementation = PostDto.class))
    )
    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestBody @Valid PostDto postDto,
            Authentication authentication
    ){
        return new ResponseEntity<>(this.postService.createPost(postDto, authentication.getName()), HttpStatus.CREATED);
    }

    //update post
    @Operation(
            summary = "Update a post by postId",
            description = "This API is used to update a post by postId and is created by the current user who is loggedIn"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = PostDto.class))
    )
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable("id") Long postId,
            @RequestBody @Valid PostDto postDto,
            Authentication authentication
    ){
        return new ResponseEntity<>(this.postService.updatePost(postDto, postId, authentication.getName()), HttpStatus.OK);
    }

    //delete post
    @Operation(
            summary = "delete a post by postId",
            description = "This API is used to delete a post by postId and is created by the current user who is loggedIn"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable("id") Long postId,
            Authentication authentication
    ){
        this.postService.deletePost(postId, authentication.getName());
        return new ResponseEntity<>("Post deleted successfully!!", HttpStatus.OK);
    }
}
