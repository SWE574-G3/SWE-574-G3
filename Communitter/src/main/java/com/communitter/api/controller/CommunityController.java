package com.communitter.api.controller;

import com.communitter.api.community.Community;
import com.communitter.api.community.CommunityService;
import com.communitter.api.community.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
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
}
