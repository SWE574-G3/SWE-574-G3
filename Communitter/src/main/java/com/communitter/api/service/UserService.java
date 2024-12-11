package com.communitter.api.service;

import com.communitter.api.dto.UserFollowDto;
import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.key.UserFollowKey;
import com.communitter.api.mapper.UserMapper;
import com.communitter.api.model.User;
import com.communitter.api.model.UserFollow;
import com.communitter.api.repository.UserFollowRepository;
import com.communitter.api.repository.UserRepository;
import java.util.Date;
import java.util.List;
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
    public List<UserFollowDto> getFollowersByFollowee(User followee) {

        List<UserFollow> followers = userFollowRepository.findAllByFollowee(followee);

        return followers.stream().map(userMapper::toUserFollowDto).toList();
    }

    @Transactional
    public List<UserFollowDto> getFolloweesByFollower(User follower) {

        List<UserFollow> followers = userFollowRepository.findAllByFollower(follower);

        return followers.stream().map(userMapper::toUserFollowDto).toList();
    }

    @Transactional
    public UserFollowDto follow(User follower, Long followeeId) {

        UserFollowKey userFollowKey = new UserFollowKey(follower.getId(), followeeId);
        if (userFollowRepository.findById(userFollowKey).isPresent()) {
            throw new RuntimeException("You are already followed this user");
        }

        User followee = userRepo.findById(followeeId).orElseThrow();

        UserFollow userFollow = userFollowRepository.save(
            UserFollow.builder()
                .id(userFollowKey)
                .followedAt(new Date())
                .build());

        return userMapper.toUserFollowDto(userFollow);
    }

    @Transactional
    public void unfollow(User follower, Long followeeId) {

        UserFollowKey userFollowKey = new UserFollowKey(follower.getId(), followeeId);
        UserFollow userFollow = userFollowRepository.findById(userFollowKey).orElseThrow();

        userFollowRepository.delete(userFollow);
    }
}
