package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.PostDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto, String userEmail) {
        User user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new ResourceNotFoundException("User","email",userEmail));
        Category category = this.categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));

        postDto.setTitle(postDto.getTitle().toUpperCase());
        if(this.postRepository.existsByTitle(postDto.getTitle()))
            throw new BlogApiException("Post title already exists!!", HttpStatus.BAD_REQUEST);

        postDto.setCreatedDate(new Date());
        Post post = this.modelMapper.map(postDto, Post.class);
        post.setCategory(category);
        post.setUser(user);
        Post savedpost = this.postRepository.save(post);
        return this.modelMapper.map(savedpost, PostDto.class);
    }

    @Override
    public PageResponse<PostDto> getAllPosts(Integer pageNum, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(AppConstants.ASCENDING) ?
                    Sort.by(sortBy).ascending()  :
                    Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Page<Post> pagedPosts = this.postRepository.findAll(page);
        List<Post> posts = pagedPosts.getContent();

        List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

        PageResponse<PostDto> pagedPostResponse = new PageResponse<PostDto>(
                postDtos,
                pagedPosts.getNumber(),
                pagedPosts.getSize(),
                pagedPosts.getTotalElements(),
                pagedPosts.getTotalPages(),
                pagedPosts.isLast()
                );

        return pagedPostResponse;
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        System.out.println(post.getComments());
        return this.modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long postId, String userEmail) {
        Category category = this.categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));

        if(!(userEmail.equals(post.getUser().getEmail())))
            throw new BlogApiException("Post don't belong to current user!! Access Denied", HttpStatus.FORBIDDEN);

        postDto.setTitle(postDto.getTitle().toUpperCase());
        if(this.postRepository.existsByTitle(postDto.getTitle()))
            throw new BlogApiException("Post title already exists!!", HttpStatus.BAD_REQUEST);

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setUpdatedDate(new Date());
        post.setCategory(category);

        Post updatedPost = this.postRepository.save(post);
        return this.modelMapper.map(updatedPost, PostDto.class);
    }

    @Override
    public void deletePost(Long postId, String userEmail) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        if(!(userEmail.equals(post.getUser().getEmail())))
            throw new BlogApiException("Post don't belong to current user!! Access Denied", HttpStatus.FORBIDDEN);
        this.postRepository.delete(post);
    }

    @Override
    public PageResponse<PostDto> getAllPostsByCategory(Long categoryId, Integer pageNum, Integer pageSize, String sortBy, String sortDir) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id", categoryId));

        Sort sort = sortDir.equalsIgnoreCase(AppConstants.DEFAULT_SORT_DIR_VALUE)?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable p = PageRequest.of(pageNum, pageSize, sort);
        Page<Post> pagedPosts = this.postRepository.findByCategoryId(category.getId(), p);
        List<Post> posts = pagedPosts.getContent();

        List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

        PageResponse<PostDto> pagedPostResponse = new PageResponse<PostDto>(
                postDtos,
                pagedPosts.getNumber(),
                pagedPosts.getSize(),
                pagedPosts.getTotalElements(),
                pagedPosts.getTotalPages(),
                pagedPosts.isLast()
        );

        return pagedPostResponse;
    }

    @Override
    public PageResponse<PostDto> getAllPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));

        Sort sort = sortDir.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagedPosts = this.postRepository.findByUser(user,p);
        List<Post> posts = pagedPosts.getContent();
        List<PostDto> postDtos =  posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        PageResponse<PostDto> postResponse = new PageResponse<>(
                postDtos,
                pagedPosts.getNumber(),
                pagedPosts.getSize(),
                pagedPosts.getTotalElements(),
                pagedPosts.getTotalPages(),
                pagedPosts.isLast()
        );

        return postResponse;
    }

    @Override
    public PageResponse<PostDto> searchPosts(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<Post> pagedPosts = this.postRepository.findByTitleContaining(keyword, p);
        List<Post> posts = pagedPosts.getContent();
        List<PostDto> postDtos =  posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        PageResponse<PostDto> postResponse = new PageResponse<>(
                postDtos,
                pagedPosts.getNumber(),
                pagedPosts.getSize(),
                pagedPosts.getTotalElements(),
                pagedPosts.getTotalPages(),
                pagedPosts.isLast()
        );

        return postResponse;
    }
}
