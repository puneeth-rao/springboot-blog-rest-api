package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCategoryId(Long categoryId, Pageable p);

    //to get all posts by a user
    Page<Post> findByUser(User user, Pageable p);

    //search post by title keyword
    Page<Post> findByTitleContaining(String keyword, Pageable p);

    Boolean existsByTitle(String postTitle);
}