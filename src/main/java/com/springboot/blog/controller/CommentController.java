package com.springboot.blog.controller;

import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.CategoryDto;
import com.springboot.blog.payload.dto.CommentDto;
import com.springboot.blog.service.AuthService;
import com.springboot.blog.service.CommentService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
//below annotation is for swagger-ui
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Comment Api's")
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //get all comments by postId
    @Operation(
            summary = "Get all comments by postId",
            description = "This API is used to get all comments by postId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PageResponse<CommentDto>> getAllCommentsByPostId(
            @PathVariable("postId") Long postId,
            @RequestParam(name = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUM_VALUE, required = false) Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE_VALUE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY_VALUE, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR_VALUE, required = false) String sortDir
    ){
        return new ResponseEntity<>(this.commentService.getAllCommentsByPostId(postId,pageNum,pageSize,sortBy,sortDir), HttpStatus.OK);
    }

    @Operation(
            summary = "Create comments by postId",
            description = "This API is used to create comment by postId"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED",
            content = @Content(schema = @Schema(implementation = CommentDto.class))
    )
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CommentDto commentDto,
            Authentication authentication
    ){
        return new ResponseEntity<>(this.commentService.createComment(postId, commentDto, authentication.getName()), HttpStatus.CREATED);
    }

    //update comment
    @Operation(
            summary = "Update a comment by commentId",
            description = "This API is used to update a comment by commentId that belongs to the given postId and is created by the current user who is loggedIn"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = CommentDto.class))
    )
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody @Valid CommentDto commentDto,
            Authentication authentication
    ){
        return new ResponseEntity<>(this.commentService.updateComment(postId, commentId, commentDto, authentication.getName()), HttpStatus.OK );
    }

    @Operation(
            summary = "delete a comment by commentId",
            description = "This API is used to delete a comment by commentId and is created by the current user who is loggedIn"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable("commentId") Long commentId,
            Authentication authentication
    ){
        this.commentService.deleteCommentById(commentId, authentication.getName());
        return new ResponseEntity<>("Comment deleted successfully!!", HttpStatus.OK);
    }
}
