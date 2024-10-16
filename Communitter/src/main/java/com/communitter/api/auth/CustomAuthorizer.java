package com.communitter.api.auth;

import com.communitter.api.key.SubscriptionKey;
import com.communitter.api.model.Community;
import com.communitter.api.model.Subscription;
import com.communitter.api.model.Post;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.PostRepository;
import com.communitter.api.repository.SubscriptionRepository;
import com.communitter.api.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component("authorizer")
@RequiredArgsConstructor
public class CustomAuthorizer {
    private final CommunityRepository communityRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PostRepository postRepository;
    public Logger logger = LoggerFactory.getLogger(CustomAuthorizer.class);
    public boolean authorizerForUser(MethodSecurityExpressionOperations operations, Long id){
        User principal= extractPrincipal(operations);
        Long principalId = principal.getId();
        logger.info(principalId.toString());
        logger.info(String.valueOf(principalId.equals(id)));
        return principalId.equals(id);
    }

    public boolean checkSubscription(MethodSecurityExpressionOperations operations, Long id){
        User principal= extractPrincipal(operations);
        Community community = communityRepository.findById(id).orElseThrow();
        SubscriptionKey subsKey= new SubscriptionKey(principal.getId(), community.getId());
        return subscriptionRepository.findById(subsKey).isPresent();
    }
    public boolean checkAuthor(MethodSecurityExpressionOperations operations, Long id){
        User principal= extractPrincipal(operations);
        Post post = postRepository.findById(id).orElseThrow();
        return Objects.equals(principal.getId(), post.getAuthor().getId());
    }

    public boolean checkCreator(MethodSecurityExpressionOperations operations, Long id){
        User principal= extractPrincipal(operations);
        Community community = communityRepository.findById(id).orElseThrow();
        SubscriptionKey subsKey= new SubscriptionKey(principal.getId(), community.getId());
        Optional<Subscription> sub =subscriptionRepository.findById(subsKey);
        if(sub.isPresent()){
            return sub.get().getRole().getName().equalsIgnoreCase("creator");
        }else{
            return false;
        }

    }

    private User extractPrincipal(MethodSecurityExpressionOperations operations){
        Authentication authentication=operations.getAuthentication();
        return (User)authentication.getPrincipal();
    }


}
