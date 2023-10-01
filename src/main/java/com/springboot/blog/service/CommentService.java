package com.springboot.blog.service;

import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(Long postId, CommentDto commentDto, String userEmail);
    PageResponse<CommentDto> getAllCommentsByPostId(Long postId, Integer pageNum, Integer pageSize, String sortBy, String sortDir);
    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto, String userEmail);
    void deleteCommentById(Long commentId, String userEmail);
 }
