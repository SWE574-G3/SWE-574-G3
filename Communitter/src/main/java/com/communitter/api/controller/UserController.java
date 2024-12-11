package com.communitter.api.controller;

import com.communitter.api.dto.UserFollowDto;
import com.communitter.api.dto.ImageDTO;
import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.model.User;
import com.communitter.api.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.communitter.api.service.ImageService;
import java.io.IOException;


@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserInfo(@P ("id") @PathVariable Long id){
        return ResponseEntity.ok(userService.getUserInfo(id));
    }
    @DeleteMapping ("/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<String> deleteAccount(@P ("id") @PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted");
    }
    @PutMapping("/update/profile/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserProfile(@P ("id") @PathVariable Long id, UserRequest userInfo){
        return ResponseEntity.ok(userService.updateUserProfile(id, userInfo));
    }

    @PutMapping("/update/email/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserEmail(@P ("id") @PathVariable Long id, UserRequest userInfo){
        return ResponseEntity.ok(userService.updateUserEmail(id, userInfo));
    }

    @PutMapping("/update/password/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserPassword(@P ("id") @PathVariable Long id, UserRequest userInfo){
        return ResponseEntity.ok(userService.updateUserPassword(id, userInfo));
    }

    //Ahmet Ahmet
    @PutMapping("/update/header/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserHeader(@P("id") @PathVariable Long id, @RequestParam String header) {
        return ResponseEntity.ok(userService.updateUserHeader(id, header));
    }

    @PutMapping("/update/about/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserAbout(@P("id") @PathVariable Long id, @RequestParam String about) {
        return ResponseEntity.ok(userService.updateUserAbout(id, about));
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserFollowDto>> getFollowers(@AuthenticationPrincipal User authUser){
        return ResponseEntity.ok(userService.getFollowersByFollowee(authUser));
    }

    @GetMapping("/followees")
    public ResponseEntity<List<UserFollowDto>> getFollowees(@AuthenticationPrincipal User authUser){
        return ResponseEntity.ok(userService.getFolloweesByFollower(authUser));
    }

    @PostMapping("/follow")
    public ResponseEntity<UserFollowDto> follow(@AuthenticationPrincipal User authUser, @RequestParam("followee-id") Long followeeId){
        return ResponseEntity.ok(userService.follow(authUser, followeeId));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal User authUser, @RequestParam("followee-id") Long followeeId){
        userService.unfollow(authUser, followeeId);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("hello");
    }

}
