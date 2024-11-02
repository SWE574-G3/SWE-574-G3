package com.communitter.api.controller;

import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.model.User;
import com.communitter.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("hello");
    }

}
