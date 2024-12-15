package com.communitter.api.service;

import com.communitter.api.dto.CommunityDto;
import com.communitter.api.dto.CommunityLabelDto;
import com.communitter.api.model.*;
import com.communitter.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    @InjectMocks
    private RecommendationService recommendationService;

    @Mock
    private UserInterestRepository userInterestRepository;

    @Mock
    private WikiEntityRepository wikiEntityRepository;

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private CommunityLabelRepository communityLabelRepository;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Set up mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        // Mock SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void deleteUserInterest_shouldDeleteInterest() {
        WikiEntity wikiEntity = new WikiEntity();
        wikiEntity.setCode("entity1");
        wikiEntity.setLabel("Test Label");

        UserInterest userInterest = new UserInterest();
        userInterest.setUser(mockUser);
        userInterest.setWikiEntity(wikiEntity);

        when(wikiEntityRepository.findByCode("entity1")).thenReturn(Optional.of(wikiEntity));
        when(userInterestRepository.findByUserAndWikiEntity(mockUser, wikiEntity)).thenReturn(Optional.of(userInterest));

        recommendationService.deleteUserInterest("entity1");

        verify(userInterestRepository).delete(userInterest);
    }
    @Test
    void getCommunityRecommendations_shouldReturnRecommendations() {
        WikiEntity wikiEntity = new WikiEntity();
        wikiEntity.setCode("entity1");
        wikiEntity.setLabel("Test Label");
        wikiEntity.setParentCodes(Set.of("parent1"));

        UserInterest userInterest = new UserInterest();
        userInterest.setUser(mockUser);
        userInterest.setWikiEntity(wikiEntity);

        Community community = new Community();
        community.setId(1L);
        community.setName("Community1");

        User communityCreator = new User();
        communityCreator.setId(2L);
        communityCreator.setUsername("creatorUser");

        community.setCreator(communityCreator); // Set the creator field to a valid User instance

        CommunityLabel communityLabel = new CommunityLabel();
        communityLabel.setCommunity(community);
        communityLabel.setWikiEntity(wikiEntity);

        when(userInterestRepository.findAllByUser(mockUser)).thenReturn(Collections.singletonList(userInterest));
        when(communityLabelRepository.findAllByWikiEntityIn(anySet())).thenReturn(Optional.of(Collections.singleton(communityLabel)));

        Set<CommunityDto> result = recommendationService.getCommunityRecommendations();

        assertNotNull(result);
        assertEquals(1, result.size());

        CommunityDto communityDto = result.iterator().next();
        assertEquals("Community1", communityDto.getName());
        assertEquals(1L, communityDto.getId());
        assertEquals(2L, communityDto.getCreator().getId());
        assertEquals("creatorUser", communityDto.getCreator().getUsername());

        verify(userInterestRepository).findAllByUser(mockUser);
        verify(communityLabelRepository).findAllByWikiEntityIn(anySet());
    }


    @Test
    void getCommunityLabel_shouldReturnLabels() {
        WikiEntity wikiEntity = new WikiEntity();
        wikiEntity.setCode("entity1");
        wikiEntity.setLabel("Test Entity");

        Community mockCommunity = new Community();
        mockCommunity.setId(1L);

        CommunityLabel communityLabel = new CommunityLabel();
        communityLabel.setId(1L);
        communityLabel.setCommunity(mockCommunity);
        communityLabel.setWikiEntity(wikiEntity);

        when(communityRepository.findById(1L)).thenReturn(Optional.of(mockCommunity));
        when(communityLabelRepository.findAllByCommunity(mockCommunity)).thenReturn(Collections.singletonList(communityLabel));

        List<CommunityLabelDto> result = recommendationService.getCommunityLabel(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("entity1", result.get(0).getWikiEntity().getCode());
        verify(communityRepository).findById(1L);
        verify(communityLabelRepository).findAllByCommunity(mockCommunity);
    }

    @Test
    void saveCommunityLabel_shouldSaveLabels() {
        WikiEntity wikiEntity = new WikiEntity();
        wikiEntity.setCode("entity1");
        wikiEntity.setLabel("Test Entity");

        Community mockCommunity = new Community();
        mockCommunity.setId(1L);

        CommunityLabel communityLabel = new CommunityLabel();
        communityLabel.setWikiEntity(wikiEntity);
        communityLabel.setCommunity(mockCommunity);

        when(wikiEntityRepository.findByCode("entity1")).thenReturn(Optional.of(wikiEntity));
        when(communityRepository.findById(1L)).thenReturn(Optional.of(mockCommunity));
        when(communityLabelRepository.saveAll(anyList())).thenReturn(Collections.singletonList(communityLabel));

        List<CommunityLabel> result = recommendationService.saveCommunityLabel(Collections.singletonList(communityLabel), 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(communityLabelRepository).saveAll(anyList());
    }
}
