package com.communitter.api.controller;

import com.communitter.api.dto.UserFollowInfoDto;
import com.communitter.api.dto.UserFollowerOrFolloweeDto;
import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.model.User;
import com.communitter.api.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserInfo(@P("id") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<String> deleteAccount(@P("id") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted");
    }

    @PutMapping("/update/profile/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserProfile(@P("id") @PathVariable Long id,
        UserRequest userInfo) {
        return ResponseEntity.ok(userService.updateUserProfile(id, userInfo));
    }

    @PutMapping("/update/email/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserEmail(@P("id") @PathVariable Long id,
        UserRequest userInfo) {
        return ResponseEntity.ok(userService.updateUserEmail(id, userInfo));
    }

    @PutMapping("/update/password/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserPassword(@P("id") @PathVariable Long id,
        UserRequest userInfo) {
        return ResponseEntity.ok(userService.updateUserPassword(id, userInfo));
    }

    //Ahmet Ahmet
    @PutMapping("/update/header/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserHeader(@P("id") @PathVariable Long id,
        @RequestParam String header) {
        return ResponseEntity.ok(userService.updateUserHeader(id, header));
    }

    @PutMapping("/update/about/{id}")
    @PreAuthorize("@authorizer.authorizerForUser(#root,#id)")
    public ResponseEntity<User> updateUserAbout(@P("id") @PathVariable Long id,
        @RequestParam String about) {
        return ResponseEntity.ok(userService.updateUserAbout(id, about));
    }

    @GetMapping("/{id}/follow-info")
    public ResponseEntity<UserFollowInfoDto> getFollowInfo(@AuthenticationPrincipal User authUser,
        @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserFollowInfo(id, authUser.getId()));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserFollowerOrFolloweeDto>> getFollowers(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFollowersByFolloweeId(id));
    }

    @GetMapping("/{id}/followees")
    public ResponseEntity<List<UserFollowerOrFolloweeDto>> getFollowees(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFolloweesByFollowerId(id));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<String> follow(@AuthenticationPrincipal User authUser,
        @PathVariable Long id) {
        userService.follow(authUser, id);
        return ResponseEntity.ok("Successfully followed user with ID " + id);
    }

    @DeleteMapping("/{id}/unfollow")
    public ResponseEntity<String> unfollow(@AuthenticationPrincipal User authUser,
        @PathVariable Long id) {
        userService.unfollow(authUser, id);
        return ResponseEntity.ok("Successfully unfollowed user with ID " + id);
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("hello");
    }

}
