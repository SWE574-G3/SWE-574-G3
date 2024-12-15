package com.communitter.api.service;

import com.communitter.api.dto.CommunitySearchResultDto;
import com.communitter.api.dto.UserSearchResultDto;
import com.communitter.api.model.Community;
import com.communitter.api.model.User;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.UserRepository;
import com.communitter.api.response.BasicSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommunityRepository communityRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void basicSearch_shouldReturnResults_whenMatchingCommunitiesAndUsersExist() {
        // Mock community data
        Community community1 = new Community();
        community1.setId(1L);
        community1.setName("Test Community");
        community1.setAbout("A community about testing");

        Community community2 = new Community();
        community2.setId(2L);
        community2.setName("Example Community");
        community2.setAbout("This is an example");

        // Mock user data
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser");
        user1.setAbout("I am a test user");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("exampleuser");
        user2.setAbout("This is an example user");

        // Mock repository calls
        when(communityRepository.findAll()).thenReturn(Arrays.asList(community1, community2));
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Perform the search
        String input = "test";
        BasicSearchResponse response = searchService.basicSearch(input);

        // Assertions
        List<CommunitySearchResultDto> expectedCommunityResults = Collections.singletonList(
                CommunitySearchResultDto.builder().id(1L).name("Test Community").build()
        );
        List<UserSearchResultDto> expectedUserResults = Collections.singletonList(
                UserSearchResultDto.builder().id(1L).username("testuser").build()
        );

        assertEquals(expectedCommunityResults, response.getCommunities());
        assertEquals(expectedUserResults, response.getUsers());
    }

    @Test
    void basicSearch_shouldReturnEmptyResults_whenNoMatchingCommunitiesAndUsersExist() {
        // Mock empty repositories
        when(communityRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Perform the search
        String input = "nonexistent";
        BasicSearchResponse response = searchService.basicSearch(input);

        // Assertions
        assertEquals(0, response.getCommunities().size());
        assertEquals(0, response.getUsers().size());
    }
    @Test
    void basicSearch_shouldHandleCaseInsensitiveSearch() {
        // Mock community data
        Community community = new Community();
        community.setId(1L);
        community.setName("Test Community");
        community.setAbout("A community about testing");

        // Mock user data
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setAbout("I am a test user");

        // Mock repository calls
        when(communityRepository.findAll()).thenReturn(Collections.singletonList(community));
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Perform the search with different case input
        String input = "test";
        BasicSearchResponse response = searchService.basicSearch(input.toLowerCase());

        // Assertions
        assertEquals(1, response.getCommunities().size());
        assertEquals(1, response.getUsers().size());

        // Additional assertions to validate the matched results
        assertEquals("Test Community", response.getCommunities().get(0).getName());
        assertEquals("TestUser", response.getUsers().get(0).getUsername());
    }

}
