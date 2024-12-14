package com.communitter.api.service;

import com.communitter.api.dto.UserFollowInfoDto;
import com.communitter.api.dto.UserFolloweeDto;
import com.communitter.api.dto.UserFollowerDto;
import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.mapper.UserMapper;
import com.communitter.api.model.User;
import com.communitter.api.model.UserFollow;
import com.communitter.api.repository.UserFollowRepository;
import com.communitter.api.repository.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final UserFollowRepository userFollowRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public User getUserInfo(Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Transactional
    public User updateUserProfile(Long id, UserRequest request) {
        User existinguser = userRepo.findById(id).orElseThrow();
        existinguser.setAbout(request.getAbout());
        existinguser.setAvatar(request.getAvatar());
        existinguser.setHeader(request.getHeader());

        return userRepo.save(existinguser);
    }

    @Transactional
    public User updateUserEmail(Long id, UserRequest request) {
        User existinguser = userRepo.findById(id).orElseThrow();
        existinguser.setEmail(request.getEmail());

        return userRepo.save(existinguser);
    }

    @Transactional
    public User updateUserPassword(Long id, UserRequest request) {
        User existinguser = userRepo.findById(id).orElseThrow();
        existinguser.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepo.save(existinguser);
    }

    @Transactional
    public User updateUserHeader(Long id, String header) {
        User existingUser = userRepo.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User with ID " + id + " not found"));
        existingUser.setHeader(header); // Update the header
        return userRepo.save(existingUser); // Save and return the updated User object
    }

    @Transactional
    public User updateUserAbout(Long id, String about) {
        User existingUser = userRepo.findById(id).orElseThrow();
        existingUser.setAbout(about);
        return userRepo.save(existingUser);
    }

    @Transactional
    public List<UserFollowerDto> getFollowersByFollowee(Long followeeId) {

        List<UserFollow> followers = userFollowRepository.findAllByFolloweeId(followeeId);

        return followers.stream().map(userMapper::toUserFollowerDto).toList();
    }

    @Transactional
    public List<UserFolloweeDto> getFolloweesByFollower(Long followerId) {

        List<UserFollow> followees = userFollowRepository.findAllByFollowerId(followerId);

        return followees.stream().map(userMapper::toUserFolloweeDto).toList();
    }

    @Transactional
    public void follow(User follower, Long followeeId) {

        if(Objects.equals(follower.getId(), followeeId)){
            throw new RuntimeException("You cannot follow yourself");
        }

        if (userFollowRepository.findByFollowerIdAndFolloweeId(follower.getId(),
            followeeId).isPresent()) {
            throw new RuntimeException("You are already followed this user");
        }

        User followee = userRepo.findById(followeeId).orElseThrow();

        userFollowRepository.save(
            userMapper.toUserFollowEntity(follower, followee, new Date()));
    }

    @Transactional
    public void unfollow(User follower, Long followeeId) {

        if(Objects.equals(follower.getId(), followeeId)){
            throw new RuntimeException("You cannot unfollow yourself");
        }

        UserFollow userFollow = userFollowRepository.findByFollowerIdAndFolloweeId(follower.getId(),
            followeeId).orElseThrow();

        userFollowRepository.delete(userFollow);
    }

    public UserFollowInfoDto getUserFollowInfo(Long userId, Long currentUserId) {
        // Count followers
        Long followerCount = userFollowRepository.countByFolloweeId(userId);

        // Count followees
        Long followeeCount = userFollowRepository.countByFollowerId(userId);

        // Check if current user is following the target user
        boolean followed = userFollowRepository.existsByFollowerIdAndFolloweeId(currentUserId,
            userId);

        // Build the DTO
        return UserFollowInfoDto.builder()
            .followerCount(followerCount)
            .followeeCount(followeeCount)
            .followed(followed)
            .build();
    }
}
