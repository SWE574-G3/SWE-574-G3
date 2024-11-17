package com.communitter.api.service;

import com.communitter.api.model.*;
import com.communitter.api.repository.*;
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
    private  final DataFieldTypeRepository dataFieldTypeRepository;
    private final DataFieldRepository dataFieldRepository;
    private final TemplateRepository templateRepository;
    public Logger logger = LoggerFactory.getLogger(CommunityService.class);

    @Transactional
    public Community createCommunity(Community community){
        logger.info(String.valueOf(community));
        User creator= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        community.setCreator(creator);
        Community createdCommunity=communityRepository.save(community);
        SubscriptionKey subsKey= new SubscriptionKey(creator.getId(), createdCommunity.getId());
        Role creatorRole= roleRepository.findByName("creator").orElseThrow();
        subscriptionRepository.save(new Subscription(subsKey,creator,createdCommunity,creatorRole));
        addDefaultTemplate(createdCommunity);
        return createdCommunity;
    }

    public Community getCommunity(Long id){
        return communityRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Subscription subscribeToCommunity(Long id, String roleName, Boolean checkPrivate){
        User subscriber= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Community community = communityRepository.findById(id).orElseThrow();

        if (checkPrivate && !community.isPublic())
            throw new RuntimeException("You cannot join private communities directly.");

        SubscriptionKey subsKey= new SubscriptionKey(subscriber.getId(), community.getId());
        Role userRole= roleRepository.findByName(roleName).orElseThrow();
        Optional<Subscription> currentSub=subscriptionRepository.findById(subsKey);
        if(currentSub.isPresent()) throw new RuntimeException("User already subscribed");
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

    private void addDefaultTemplate(Community community){
        Template createdTemplate = templateRepository.save(Template.builder()
                .name("Default Template")
                .community(community)
                .build());
        dataFieldRepository.save(DataField.builder().name("Title")
                .isRequired(true)
                .dataFieldType(dataFieldTypeRepository.findByType("string"))
                .template(createdTemplate)
                .build());
        dataFieldRepository.save(DataField.builder().name("Comment")
                .isRequired(true)
                .dataFieldType(dataFieldTypeRepository.findByType("string"))
                .template(createdTemplate)
                .build());
    }
}
