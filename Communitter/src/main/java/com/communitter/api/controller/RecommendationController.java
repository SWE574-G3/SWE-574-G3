package com.communitter.api.controller;

import com.communitter.api.model.UserInterest;
import com.communitter.api.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/user/interests")
    public ResponseEntity<List<UserInterest>> saveUserInterests(List<UserInterest> interests){
        return ResponseEntity.ok(recommendationService.saveUserInterest(interests));
    }
}
