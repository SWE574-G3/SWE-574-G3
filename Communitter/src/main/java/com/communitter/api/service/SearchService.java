package com.communitter.api.service;

import com.communitter.api.dto.CommunitySearchResultDto;
import com.communitter.api.dto.UserSearchResultDto;
import com.communitter.api.model.Community;
import com.communitter.api.model.User;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.UserRepository;
import com.communitter.api.response.BasicSearchResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    public BasicSearchResponse basicSearch(String input) {
        List<Community> communities = communityRepository.findAll();
        List<User> users = userRepository.findAll();

        List<CommunitySearchResultDto> communitySearchResults = new ArrayList<>();
        List<UserSearchResultDto> userSearchResults = new ArrayList<>();
        for (Community community : communities) {
            if ((community.getName() != null && community.getName().toLowerCase().contains(input))
                || (community.getAbout() != null && community.getAbout().toLowerCase()
                .contains(input))) {
                communitySearchResults.add(CommunitySearchResultDto.builder().id(community.getId())
                    .name(community.getName()).build());
            }
        }
        for (User user : users) {
            if ((user.getUsername() != null && user.getUsername().toLowerCase().contains(input))
                || (user.getAbout() != null && user.getAbout().toLowerCase()
                .contains(input))) {
                userSearchResults.add(
                    UserSearchResultDto.builder().id(user.getId()).username(user.getUsername())
                        .build());
            }
        }
        return new BasicSearchResponse(communitySearchResults, userSearchResults);
    }
}
