package com.communitter.api.service;

import com.communitter.api.dto.CommunityDto;
import com.communitter.api.model.*;
import com.communitter.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserInterestRepository userInterestRepository;
    private final WikiEntityRepository wikiEntityRepository;
    private final CommunityRepository communityRepository;
    private final CommunityLabelRepository communityLabelRepository;
    public Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    @Transactional
    public List<UserInterest> saveUserInterest(List<UserInterest> userInterests){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (UserInterest interest:userInterests){
            interest.setWikiEntity(wikiEntityRepository.findByCode(interest.getWikiEntity().getCode()).orElse(interest.getWikiEntity()));
            interest.setUser(user);
        }
       return userInterestRepository.saveAll(userInterests);
    }

    @Transactional
    public List<CommunityLabel> saveCommunityLabel(List<CommunityLabel> communityLabels,Long communityId){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (CommunityLabel label:communityLabels){
            label.setWikiEntity(wikiEntityRepository.findByCode(label.getWikiEntity().getCode()).orElse(label.getWikiEntity()));
            label.setCommunity(communityRepository.getReferenceById(communityId));
        }
        return communityLabelRepository.saveAll(communityLabels);
    }

    public Set<CommunityDto> getCommunityRecommendations(){
        User principal= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserInterest> userInterests=userInterestRepository.findAllByUser(principal);
        logger.info(userInterests.toString());
        List<String> interestCodes=new ArrayList<String>();
        for(UserInterest interest:userInterests){
            interestCodes.add(interest.getWikiEntity().getCode());
            interestCodes.addAll(interest.getWikiEntity().getParentCodes());
        }
        logger.info(interestCodes.toString());
        Set<String> interestSet= new HashSet<>(interestCodes);
        Set<WikiEntity> entities=new HashSet<>();
        for(String interestCode:interestSet){
            entities.add(WikiEntity.builder().code(interestCode).build());
        }
        Set<CommunityLabel> matchedCommunities= communityLabelRepository.findAllByWikiEntityIn(entities).orElseThrow();
        logger.info(matchedCommunities.toString());
        Set<CommunityDto> mappedCommunities= new HashSet<>();

        for(CommunityLabel communityLabel:matchedCommunities){
            Community community=communityLabel.getCommunity();
            mappedCommunities.add(CommunityDto.builder().name(community.getName()).
                    id(community.getId()).about(community.getAbout())
                    .creator(community.getCreator()).isPublic(community.isPublic()).build());
        }
        return mappedCommunities;
    }

    public List<UserInterest> getUserInterest(){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInterestRepository.findAllByUser(user);
    }

    public List<CommunityLabel> getCommunityLabel(Long communityId){
        Community community=communityRepository.findById(communityId).orElseThrow();
        return communityLabelRepository.findAllByCommunity(community);
    }

    public void deleteUserInterest(String wikiEntityCode){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WikiEntity entity=wikiEntityRepository.findByCode(wikiEntityCode).orElseThrow();
        UserInterest interest=userInterestRepository.findByUserAndWikiEntity(user, entity).orElseThrow();
        userInterestRepository.delete(interest);
    }

    public void deleteCommunityLabel(String wikiEntityCode,Long communityId){
        WikiEntity entity=wikiEntityRepository.findByCode(wikiEntityCode).orElseThrow();
        Community community=communityRepository.findById(communityId).orElseThrow();
        CommunityLabel communityLabel =communityLabelRepository.findByCommunityAndWikiEntity(community, entity).orElseThrow();
        communityLabelRepository.delete(communityLabel);
    }
}
