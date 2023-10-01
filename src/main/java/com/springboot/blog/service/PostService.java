package com.springboot.blog.service;

import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto, String userEmail);
    PageResponse<PostDto> getAllPosts(Integer pageNum, Integer pageSize, String sortBy, String sortDir);
    PostDto getPostById(Long postId);
    PostDto updatePost(PostDto postDto, Long postId, String userEmail);
    void deletePost(Long postId, String userEmail);
    PageResponse<PostDto> getAllPostsByCategory(Long categoryId, Integer pageNum, Integer pageSize, String sortBy, String sortDir);
    PageResponse<PostDto> getAllPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PageResponse<PostDto> searchPosts(String keyword,Integer pageNumber, Integer pageSize);
}
