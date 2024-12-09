package com.communitter.api.controller;

import com.communitter.api.exception.NotAuthorizedException;
import com.communitter.api.model.Community;
import com.communitter.api.service.CommunityService;
import com.communitter.api.model.Subscription;
import com.communitter.api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Community> createCommunity(@RequestBody Community community){

        return ResponseEntity.ok(communityService.createCommunity(community));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunity (@PathVariable Long id){
        return ResponseEntity.ok(communityService.getCommunity(id));
    }

    @PostMapping("/subscribe/{id}")
    public ResponseEntity<Subscription> subscribeToCommunity(@PathVariable Long id){
        return ResponseEntity.ok(communityService.subscribeToCommunity(id));
    }

    @DeleteMapping("/unsubscribe/{id}")
    public ResponseEntity<String> unsubscribeFromCommunity(@PathVariable Long id){
        return ResponseEntity.ok(communityService.unsubscribe(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Community>> getAllCommunities(){
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    @DeleteMapping("/{communityId}/deletePost/{id}")
    public ResponseEntity<String> deletePostInCommunity(@PathVariable Long communityId,@PathVariable Long id) {
        try {
            postService.deletePost(communityId, id);
            return ResponseEntity.ok("Post is deleted succesfully!");
        }catch (NotAuthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
