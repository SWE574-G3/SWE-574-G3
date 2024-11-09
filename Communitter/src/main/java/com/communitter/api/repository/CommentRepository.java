package com.communitter.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.communitter.api.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    
}
