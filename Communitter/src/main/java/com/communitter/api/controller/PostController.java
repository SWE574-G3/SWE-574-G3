package com.communitter.api.controller;


import com.communitter.api.model.Community;
import com.communitter.api.model.Post;
import com.communitter.api.model.PostVote;
import com.communitter.api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("@authorizer.checkSubscription(#root,#id)")
    @PostMapping("/community/{id}")
    public ResponseEntity<Post> createPost(@P("id") @PathVariable Long id, @RequestBody  Post post){
       return ResponseEntity.ok(postService.createPost(id, post));
    }

    public ResponseEntity<Post> getPostById(@P("id") @PathVariable Long id, Post post){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PreAuthorize("@authorizer.checkSubscription(#root,#id)")
    @PostMapping("/{id}/vote")
    public ResponseEntity<PostVote> votePost(@P("id") @PathVariable Long id, @RequestParam  boolean isUpvote){
        return ResponseEntity.ok(postService.votePost(id, isUpvote));
    }

    @GetMapping("/{id}/voteCount")
    public ResponseEntity<Long> getVoteCount(@P("id") @PathVariable Long id){
        return ResponseEntity.ok(postService.getVoteCount(id));
    }
}
