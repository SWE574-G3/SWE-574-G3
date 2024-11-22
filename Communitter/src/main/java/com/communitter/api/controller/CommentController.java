package com.communitter.api.controller;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.communitter.api.dto.CommentDto;
import com.communitter.api.model.Comment;
import com.communitter.api.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;

    @PostMapping("/posts/{id}")
    public ResponseEntity<CommentDto> createComment(@P("id") @PathVariable Long id, @RequestBody CommentDto comment){
        return ResponseEntity.ok(commentService.createComment(id, comment));
    }
    
    @PreAuthorize("@authorizer.checkCommentAuthor(#root,#id)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.deleteComment(id));
    }

    @PreAuthorize("@authorizer.checkCommentAuthor(#root,#id)")
    @PostMapping("/edit/{id}")
    public ResponseEntity<CommentDto> editComment(@RequestBody CommentDto editedComment,@P("id") @PathVariable Long id){
        return ResponseEntity.ok(commentService.editComment(editedComment));
    }

    @GetMapping("/all/{postId}")
    public ResponseEntity<Set<Comment>> getAllPostComments(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getAllPostComments(postId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getCommentById(id));
    }
}
