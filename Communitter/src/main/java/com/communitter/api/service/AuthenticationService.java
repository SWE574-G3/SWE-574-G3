package com.communitter.api.service;

import com.communitter.api.dto.request.AuthenticationRequest;
import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.response.AuthenticationResponse;
import com.communitter.api.response.RegisterResponse;
import com.communitter.api.model.User;
import com.communitter.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterResponse register(UserRequest request){
        User user= User.builder().username(request.getUsername()).password(passwordEncoder.encode(request.getPassword()))
                .about(request.getAbout()).email(request.getEmail()).avatar(request.getAvatar()).header(request.getHeader()).build();

        User createdUser=userRepository.save(user);

        String token= jwtService.generateToken(user);

        return RegisterResponse.builder().token(token).message("User created with id: "+createdUser.getId().toString()).build();

    }


    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user =userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();

    }



}
