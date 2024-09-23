package com.communitter.api.user;

import com.communitter.api.auth.AuthenticationResponse;
import com.communitter.api.auth.UserRequest;
import lombok.RequiredArgsConstructor;
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
}
