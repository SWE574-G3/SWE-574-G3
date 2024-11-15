package com.communitter.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.communitter.api.dto.InvitationDto;
import com.communitter.api.dto.request.InvitationCreateRequestDto;
import com.communitter.api.key.SubscriptionKey;
import com.communitter.api.mapper.InvitationMapper;
import com.communitter.api.model.Community;
import com.communitter.api.model.Invitation;
import com.communitter.api.model.Role;
import com.communitter.api.model.Subscription;
import com.communitter.api.model.User;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.InvitationRepository;
import com.communitter.api.repository.RoleRepository;
import com.communitter.api.repository.SubscriptionRepository;
import com.communitter.api.repository.UserRepository;
import com.communitter.api.util.InvitationStatus;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class InvitationServiceTest {

    @Mock
    private CommunityRepository communityRepository;
    @Mock
    private InvitationRepository invitationRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InvitationMapper invitationMapper;

    @InjectMocks
    private InvitationService invitationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inviteUser_ShouldThrowExceptionWhenCommunityIsPublic() {
        User authUser = new User();
        InvitationCreateRequestDto request = new InvitationCreateRequestDto();
        request.setCommunityId(1L);

        Community community = new Community();
        community.setPublic(true);

        when(communityRepository.getReferenceById(1L)).thenReturn(community);

        assertThrows(RuntimeException.class, () -> invitationService.inviteUser(authUser, request));
    }

    @Test
    void inviteUser_ShouldThrowExceptionWhenUserAlreadySubscribed() {
        User authUser = new User();
        InvitationCreateRequestDto request = new InvitationCreateRequestDto();
        request.setCommunityId(1L);
        request.setUsername("testUser");

        Community community = new Community();
        community.setPublic(false);
        User invitedUser = new User();
        invitedUser.setId(2L);

        when(communityRepository.getReferenceById(1L)).thenReturn(community);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(invitedUser));
        when(subscriptionRepository.findById(new SubscriptionKey(2L, 1L)))
            .thenReturn(Optional.of(new Subscription()));

        assertThrows(RuntimeException.class, () -> invitationService.inviteUser(authUser, request));
    }

    @Test
    void inviteUser_ShouldThrowExceptionWhenUserAlreadyInvited() {
        User authUser = new User();
        InvitationCreateRequestDto request = new InvitationCreateRequestDto();
        request.setCommunityId(1L);
        request.setUsername("testUser");

        Community community = new Community();
        community.setPublic(false);
        User invitedUser = new User();

        when(communityRepository.getReferenceById(1L)).thenReturn(community);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(invitedUser));
        when(subscriptionRepository.findById(any(SubscriptionKey.class))).thenReturn(
            Optional.empty());
        when(invitationRepository.findByCommunityAndUserAndInvitationStatus(community, invitedUser,
            InvitationStatus.PENDING))
            .thenReturn(Optional.of(new Invitation()));

        assertThrows(RuntimeException.class, () -> invitationService.inviteUser(authUser, request));
    }

    @Test
    void inviteUser_ShouldSaveInvitationAndReturnDto() {
        User authUser = new User();
        InvitationCreateRequestDto request = new InvitationCreateRequestDto();
        request.setCommunityId(1L);
        request.setUsername("testUser");
        request.setRoleId(1L);

        Community community = new Community();
        community.setPublic(false);
        User invitedUser = new User();
        Role role = new Role();
        Invitation invitation = new Invitation();
        InvitationDto invitationDto = new InvitationDto();

        when(communityRepository.getReferenceById(1L)).thenReturn(community);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(invitedUser));
        when(subscriptionRepository.findById(any(SubscriptionKey.class))).thenReturn(
            Optional.empty());
        when(invitationRepository.findByCommunityAndUserAndInvitationStatus(community, invitedUser,
            InvitationStatus.PENDING))
            .thenReturn(Optional.empty());
        when(roleRepository.getReferenceById(1L)).thenReturn(role);
        when(invitationRepository.save(any(Invitation.class))).thenReturn(invitation);
        when(invitationMapper.toDto(invitation)).thenReturn(invitationDto);

        InvitationDto result = invitationService.inviteUser(authUser, request);

        verify(invitationRepository).save(any(Invitation.class));
        assertEquals(invitationDto, result);
    }

    @Test
    void getCommunityInvitations_ShouldReturnListOfInvitations() {
        Community community = new Community();
        community.setId(1L);
        Invitation invitation = new Invitation();
        InvitationDto invitationDto = new InvitationDto();
        List<Invitation> invitations = Collections.singletonList(invitation);

        when(communityRepository.getReferenceById(1L)).thenReturn(community);
        when(invitationRepository.findAllByCommunity(community)).thenReturn(invitations);
        when(invitationMapper.toDto(invitation)).thenReturn(invitationDto);

        List<InvitationDto> result = invitationService.getCommunityInvitations(1L);

        assertEquals(1, result.size());
        assertEquals(invitationDto, result.get(0));
    }

    @Test
    void getUserPendingInvitations_ShouldReturnListOfPendingInvitations() {
        User user = new User();
        Invitation invitation = new Invitation();
        InvitationDto invitationDto = new InvitationDto();
        List<Invitation> invitations = Collections.singletonList(invitation);

        when(invitationRepository.findAllByUserAndInvitationStatus(user, InvitationStatus.PENDING))
            .thenReturn(invitations);
        when(invitationMapper.toDto(invitation)).thenReturn(invitationDto);

        List<InvitationDto> result = invitationService.getUserPendingInvitations(user);

        assertEquals(1, result.size());
        assertEquals(invitationDto, result.get(0));
    }
}
