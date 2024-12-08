package com.communitter.api.service;

import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.repository.UserRepository;
import com.communitter.api.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

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


}
