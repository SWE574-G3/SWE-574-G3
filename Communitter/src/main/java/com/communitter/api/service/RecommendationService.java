package com.communitter.api.service;

import com.communitter.api.model.CommunityLabel;
import com.communitter.api.model.User;
import com.communitter.api.model.UserInterest;
import com.communitter.api.repository.CommunityLabelRepository;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.UserInterestRepository;
import com.communitter.api.repository.WikiEntityRepository;
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
}
