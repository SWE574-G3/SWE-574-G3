package com.communitter.api.service;

import com.communitter.api.dto.ActivityStreamDto;
import com.communitter.api.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.communitter.api.model.*;
import com.communitter.api.repository.ActivityStreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityStreamService {

    private final ActivityStreamRepository activityStreamRepository;
    private final CommentRepository commentRepository;
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

    public ActivityStreamDto toDto(ActivityStream activityStream) {
        ActivityStreamDto dto = new ActivityStreamDto();
        dto.setId(activityStream.getId());
        dto.setUserId(activityStream.getUser().getId());
        dto.setUserName(activityStream.getUser().getUsername());
        dto.setCommunityId(activityStream.getCommunity().getId());
        dto.setCommunityName(activityStream.getCommunity().getName());
        dto.setTimestamp(activityStream.getTimestamp());
        dto.setAction(activityStream.getAction().name());

        if (activityStream.getPost() != null) {
            dto.setPostId(activityStream.getPost().getId());
        }

//        // Handle COMMENT action separately and fetch actual comment content
//        if (activityStream.getAction() == ActivityAction.COMMENT) {
//            if( activityStream.getPost() != null) {
//                Long commentId = activityStream.getPost().getId();
//            }// You can link the comment through the post, depending on your setup
//            Comment comment = commentRepository.findById(commentId).orElse(null);
//
//            if (comment != null) {
//                dto.setCommentContent(comment.getContent());  // Set the actual content of the comment
//            } else {
//                dto.setCommentContent("Comment not found");
//            }
//        }

        // Generate description based on action type
        switch (activityStream.getAction()) {
            case JOIN:
                dto.setDescription(dto.getUserName() + " joined " + dto.getCommunityName());
                break;
            case UPVOTE:
                dto.setDescription(dto.getUserName() + " upvoted a post");
                break;
            case DOWNVOTE:
                dto.setDescription(dto.getUserName() + " downvoted a post");
                break;
            case COMMENT:
                dto.setDescription(dto.getUserName() + " commented on a post");
                break;
            case CREATE:
                dto.setDescription(dto.getUserName() + " created a post");
                break;
        }

        return dto;
    }

    public List<ActivityStream> getActivitiesByCommunity(Long communityId) {
        return activityStreamRepository.findByCommunityId(communityId);
    }
}
