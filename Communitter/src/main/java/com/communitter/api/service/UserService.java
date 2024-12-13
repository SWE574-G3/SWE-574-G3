package com.communitter.api.service;

import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.model.Follow;
import com.communitter.api.repository.FollowRepository;
import com.communitter.api.repository.UserRepository;
import com.communitter.api.model.User;
import com.communitter.api.util.BasicAuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final BasicAuthorizationUtil basicAuthorizationUtil;

    public User getUserInfo(Long id){
        return userRepo.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteUser(Long id){
        userRepo.deleteById(id);
    }

    @Transactional
    public User updateUserProfile(Long id,UserRequest request){
        User existinguser=userRepo.findById(id).orElseThrow();
        existinguser.setAbout(request.getAbout());
        existinguser.setAvatar(request.getAvatar());
        existinguser.setHeader(request.getHeader());

        return userRepo.save(existinguser);
    }
    @Transactional
    public User updateUserEmail(Long id,UserRequest request){
        User existinguser=userRepo.findById(id).orElseThrow();
        existinguser.setEmail(request.getEmail());

        return userRepo.save(existinguser);
    }
    @Transactional
    public User updateUserPassword(Long id,UserRequest request){
        User existinguser=userRepo.findById(id).orElseThrow();
        existinguser.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepo.save(existinguser);
    }

    @Transactional
    public User updateUserHeader(Long id, String header) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with ID " + id + " not found"));
        existingUser.setHeader(header); // Update the header
        return userRepo.save(existingUser); // Save and return the updated User object
    }

    @Transactional
    public User updateUserAbout(Long id, String about) {
        User existingUser = userRepo.findById(id).orElseThrow();
        existingUser.setAbout(about);
        return userRepo.save(existingUser);
    }

    public String followUser(Long followedId) {
        User currentUser = basicAuthorizationUtil.getCurrentUser();
        Long followerId = currentUser.getId();
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("A user cannot follow themselves.");
        }

        User follower = userRepo.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found"));

        User followed = userRepo.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("Followed user not found"));

        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new IllegalStateException("You are already following this user.");
        }
        LocalDateTime now = LocalDateTime.now();
        Follow follow = Follow.builder()
                .follower(follower)
                .followed(followed)
                .followedAt(now)
                .build();
        followRepository.save(follow);

        return "User " + follower.getUsername() + " successfully followed " + followed.getUsername();
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

}
