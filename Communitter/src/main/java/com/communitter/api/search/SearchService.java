package com.communitter.api.search;

import com.communitter.api.community.Community;
import com.communitter.api.community.CommunityRepository;
import com.communitter.api.user.User;
import com.communitter.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    public BasicSearchResponse basicSearch(String input){
        List<Community> communities =communityRepository.findAll();
        List<User> users =userRepository.findAll();

        List<CommunitySearchResultDto> communitySearchResults =new ArrayList<>();
        List<UserSearchResultDto> userSearchResults =new ArrayList<>();
        for(Community community:communities){
            if(community.getName().toLowerCase().contains(input) || community.getAbout().toLowerCase().contains(input)){
                communitySearchResults.add(CommunitySearchResultDto.builder().id(community.getId()).name(community.getName()).build());
            }
        }
        for(User user:users){
            if(user.getUsername().toLowerCase().contains(input) || user.getAbout().toLowerCase().contains(input)){
                userSearchResults.add(UserSearchResultDto.builder().id(user.getId()).username(user.getUsername()).build());
            }
        }
        return new BasicSearchResponse(communitySearchResults,userSearchResults);
    }
}
