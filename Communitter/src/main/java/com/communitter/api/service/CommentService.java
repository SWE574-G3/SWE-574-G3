package com.communitter.api.service;

import java.util.Date;
import java.util.Set;

import com.communitter.api.model.ActivityAction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.communitter.api.dto.CommentAuthorDto;
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
    private final ActivityStreamService activityStreamService;

    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto){
        User author= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(postId).orElseThrow();
        Comment createdComment = Comment.builder().date(new Date())
                .post(post)
                .date(new Date())
        .author(author)          
        .content(commentDto.getContent()) 
        .build();

        commentRepository.save(createdComment);

        post.getComments().add(createdComment);
        postRepository.save(post);
        activityStreamService.createActivity(ActivityAction.COMMENT, author, post.getCommunity(), post);
        return CommentDto.builder()
        .id(createdComment.getId())
        .author(
            CommentAuthorDto.builder()
            .id(author.getId())
            .username(author.getUsername())
            .build()
        )
        .date(createdComment.getDate())
        .content(createdComment.getContent())
        .build();   
    }

    @Transactional
    public String deleteComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow();
        commentRepository.delete(comment);
        return "Comment deleted";
    }

    @Transactional
    public Comment getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        return comment;
    }

    @Transactional
    public Set<Comment> getAllPostComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        Set<Comment> comments = post.getComments();

        return comments;
    }
}
