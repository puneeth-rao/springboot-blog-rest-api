package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto, String userEmail) {
        Post post = this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id", postId));
        User user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new ResourceNotFoundException("User","email",userEmail));

        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setCreatedDate(new Date());
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment = this.commentRepository.save(comment);

        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public PageResponse<CommentDto> getAllCommentsByPostId(Long postId, Integer pageNum, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(AppConstants.ASCENDING) ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();
        Pageable p = PageRequest.of(pageNum, pageSize, sort);
        Page<Comment> pagedComments = this.commentRepository.findByPostId(postId,p);

        List<Comment> comments = pagedComments.getContent();
        List<CommentDto> commentDtos = comments
                                        .stream()
                                        .map(comment -> this.modelMapper.map(comment, CommentDto.class))
                                        .collect(Collectors.toList());

        PageResponse<CommentDto> commentResponse = new PageResponse<>(
                commentDtos,
                pagedComments.getNumber(),
                pagedComments.getSize(),
                pagedComments.getTotalElements(),
                pagedComments.getTotalPages(),
                pagedComments.isLast()
        );

        return commentResponse;
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto, String userEmail) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id", commentId));
        Post post = this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id", postId));

        if(comment.getPost().getId() != post.getId())
            throw new BlogApiException("Comment does not belong to given post", HttpStatus.BAD_REQUEST);

        if(!(userEmail.equals(comment.getUser().getEmail())))
            throw new BlogApiException("Comment don't belong to current user!! Access Denied", HttpStatus.FORBIDDEN);

        comment.setBody(commentDto.getBody());
        comment.setUpdatedDate(new Date());
        Comment updatedComment = this.commentRepository.save(comment);

        return this.modelMapper.map(updatedComment, CommentDto.class);
    }

    @Override
    public void deleteCommentById(Long commentId, String userEmail) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id", commentId));
        if(!(userEmail.equals(comment.getUser().getEmail())))
            throw new BlogApiException("Comment don't belong to current user!! Access Denied", HttpStatus.FORBIDDEN);
        this.commentRepository.delete(comment);
    }

}
