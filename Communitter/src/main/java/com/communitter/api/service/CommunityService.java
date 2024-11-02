package com.communitter.api.service;

import com.communitter.api.model.*;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.SubscriptionRepository;
import com.communitter.api.model.Role;
import com.communitter.api.repository.RoleRepository;
import com.communitter.api.model.User;
import com.communitter.api.model.Subscription;
import com.communitter.api.key.SubscriptionKey;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RoleRepository roleRepository;
    private final ActivityStreamService activityStreamService;
    public Logger logger = LoggerFactory.getLogger(CommunityService.class);

    @Transactional
    public Community createCommunity(Community community){
        User creator= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        community.setCreator(creator);
        Community createdCommunity=communityRepository.save(community);
        SubscriptionKey subsKey= new SubscriptionKey(creator.getId(), createdCommunity.getId());
        Role creatorRole= roleRepository.findByName("creator").orElseThrow();
        subscriptionRepository.save(new Subscription(subsKey,creator,createdCommunity,creatorRole));
        return createdCommunity;
    }

    public Community getCommunity(Long id){
        return communityRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Subscription subscribeToCommunity(Long id){
        User subscriber= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Community community = communityRepository.findById(id).orElseThrow();
        SubscriptionKey subsKey= new SubscriptionKey(subscriber.getId(), community.getId());
        Role userRole= roleRepository.findByName("user").orElseThrow();
        Optional<Subscription> currentSub=subscriptionRepository.findById(subsKey);
        if(currentSub.isPresent()) throw new RuntimeException("User already subscribed");
        activityStreamService.createActivity(ActivityAction.JOIN, subscriber,community, null);
        return subscriptionRepository.save(new Subscription(subsKey,subscriber,community,userRole));
    }
    @Transactional
    public String unsubscribe(Long id){
        User subscriber= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Community community = communityRepository.findById(id).orElseThrow();
        SubscriptionKey subsKey= new SubscriptionKey(subscriber.getId(), community.getId());
        Subscription currentSub=subscriptionRepository.findById(subsKey).orElseThrow(()->new NoSuchElementException("User already not subscribed"));
        if(currentSub.getRole().getName().equals("creator")) throw new RuntimeException("Creator cannot leave the community");
        subscriptionRepository.delete(currentSub);
        return "User unsubscribed";
    }
    public List<Community> getAllCommunities(){
        return communityRepository.findAll();
    }
}
