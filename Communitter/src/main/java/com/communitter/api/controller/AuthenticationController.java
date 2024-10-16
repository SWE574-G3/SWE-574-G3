package com.communitter.api.controller;

import com.communitter.api.dto.request.AuthenticationRequest;
import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.response.AuthenticationResponse;
import com.communitter.api.response.RegisterResponse;
import com.communitter.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserRequest request){
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
