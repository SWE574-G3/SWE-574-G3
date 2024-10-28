package com.communitter.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.communitter.api.model.*;
import com.communitter.api.repository.ActivityStreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityStreamService {

    private final ActivityStreamRepository activityStreamRepository;
    public Logger logger = LoggerFactory.getLogger(ActivityStreamService.class);

    public void createActivity(ActivityAction action, User user, Community community, Post post) {
        ActivityStream activityStream = new ActivityStream();
        activityStream.setAction(action);
        activityStream.setUser(user);
        activityStream.setCommunity(community);
        activityStream.setPost(post);
        activityStream.setTimestamp(LocalDateTime.now());
        logger.info("Creating activity stream");
        activityStreamRepository.save(activityStream);
        logger.info("ACTIVITY SAVED!!! " + activityStream);
    }
}
