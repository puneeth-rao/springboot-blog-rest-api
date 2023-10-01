package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long postId, Pageable p);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM comments c WHERE c.post_id=:postId")
    void deleteAllCommentsByPostId(@Param("postId") Long postId);

}
