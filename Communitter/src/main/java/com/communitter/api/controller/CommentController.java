package com.communitter.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.communitter.api.dto.CommentDto;
import com.communitter.api.service.CommentService;
import com.communitter.api.service.JwtService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    public Logger logger = LoggerFactory.getLogger(JwtService.class);

    @PreAuthorize("@authorizer.checkAuthor(#root,#id)")
    @PostMapping("/posts/{id}")
    public ResponseEntity<CommentDto> createComment(@P("id") @PathVariable Long id, @RequestBody CommentDto comment){
        logger.info("BODY:" + comment);
        return ResponseEntity.ok(commentService.createComment(id, comment));
    }

    @PreAuthorize("@authorizer.checkAuthor(#root,#id)")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}
