package com.communitter.api.service;

import com.communitter.api.model.User;
import com.communitter.api.model.UserInterest;
import com.communitter.api.repository.UserInterestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserInterestRepository userInterestRepository;
    public Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    @Transactional
    public List<UserInterest> saveUserInterest(List<UserInterest> userInterests){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (UserInterest interest:userInterests){
            interest.setUser(user);
        }
       return userInterestRepository.saveAll(userInterests);
    }
}
