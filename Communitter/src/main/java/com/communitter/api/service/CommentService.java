package com.communitter.api.service;

import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.communitter.api.dto.CommentDto;
import com.communitter.api.model.Comment;
import com.communitter.api.model.Post;
import com.communitter.api.model.User;
import com.communitter.api.repository.CommentRepository;
import com.communitter.api.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto){
        Post post = postRepository.findById(postId).orElseThrow();
        User author= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment createdComment = Comment.builder().date(new Date())        
        .post(post)              
        .author(author)          
        .content(commentDto.getContent()) 
        .build();
        commentRepository.save(createdComment);
        return commentDto;
    }

    @Transactional
    public String deleteComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow();
        commentRepository.delete(comment);
        return "Comment deleted";
    }
}
