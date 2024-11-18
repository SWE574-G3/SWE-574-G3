package com.communitter.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

    @Mock
    private CommunityService communityService;

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

    @Test
    void acceptOrRejectInvitation_ShouldAccept_WhenValidRequest() {
        User authUser = new User();
        authUser.setId(1L);

        Community community = new Community();
        community.setId(1L);

        Role role = new Role();
        role.setName("user");  // Set the role name to avoid null

        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setUser(authUser);
        invitation.setInvitationStatus(InvitationStatus.PENDING);
        invitation.setCommunity(community);
        invitation.setRole(role);  // Set the role to the invitation

        InvitationDto invitationDto = new InvitationDto();

        when(invitationRepository.findByIdAndInvitationStatus(1L, InvitationStatus.PENDING))
            .thenReturn(Optional.of(invitation));
        when(invitationMapper.toDto(invitation)).thenReturn(invitationDto);

        InvitationDto result = invitationService.acceptOrRejectInvitation(authUser, 1L, InvitationStatus.ACCEPTED);

        verify(communityService, times(1))
            .subscribeToCommunity(community.getId(), role.getName(), false);
        assertEquals(invitationDto, result);
    }

    @Test
    void acceptOrRejectInvitation_ShouldReject_WhenValidRequest() {
        User authUser = new User();
        authUser.setId(1L);

        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setUser(authUser);
        invitation.setInvitationStatus(InvitationStatus.PENDING);

        InvitationDto invitationDto = new InvitationDto();

        when(invitationRepository.findByIdAndInvitationStatus(1L, InvitationStatus.PENDING))
            .thenReturn(Optional.of(invitation));
        when(invitationMapper.toDto(invitation)).thenReturn(invitationDto);

        InvitationDto result = invitationService.acceptOrRejectInvitation(authUser, 1L,
            InvitationStatus.REJECTED);

        verify(communityService, never()).subscribeToCommunity(anyLong(), anyString(),
            anyBoolean());
        assertEquals(invitationDto, result);
    }

    @Test
    void acceptOrRejectInvitation_ShouldThrowException_WhenInvitationDoesNotBelongToUser() {
        User authUser = new User();
        authUser.setId(1L);

        User anotherUser = new User();
        anotherUser.setId(2L);

        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setUser(anotherUser);
        invitation.setInvitationStatus(InvitationStatus.PENDING);

        when(invitationRepository.findByIdAndInvitationStatus(1L, InvitationStatus.PENDING))
            .thenReturn(Optional.of(invitation));

        assertThrows(RuntimeException.class, () ->
            invitationService.acceptOrRejectInvitation(authUser, 1L, InvitationStatus.ACCEPTED));
    }

    @Test
    void cancelInvitation_ShouldCancelInvitation_WhenAuthorized() {
        User authUser = new User();
        authUser.setId(1L);

        Community community = new Community();
        community.setId(1L);

        Invitation invitation = new Invitation();
        invitation.setCommunity(community);
        invitation.setId(1L);
        invitation.setInvitationStatus(InvitationStatus.PENDING);
        invitation.setSentBy(authUser);

        Role userRole = new Role();
        userRole.setName("user");

        Subscription subscription = new Subscription();
        subscription.setRole(userRole);

        InvitationDto invitationDto = new InvitationDto();

        when(invitationRepository.findByIdAndInvitationStatus(1L, InvitationStatus.PENDING))
            .thenReturn(Optional.of(invitation));
        when(roleRepository.findByName("user")).thenReturn(Optional.of(userRole));
        when(subscriptionRepository.findById(any(SubscriptionKey.class))).thenReturn(Optional.of(subscription));
        when(invitationMapper.toDto(invitation)).thenReturn(invitationDto);

        InvitationDto result = invitationService.cancelInvitation(authUser, 1L);

        assertEquals(invitationDto, result);
        assertEquals(InvitationStatus.CANCELLED, invitation.getInvitationStatus());
    }

    @Test
    void cancelInvitation_ShouldThrowException_WhenUnauthorizedUserCancels() {
        User authUser = new User();
        authUser.setId(1L);

        User senderUser = new User();
        senderUser.setId(2L);

        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setInvitationStatus(InvitationStatus.PENDING);
        invitation.setSentBy(senderUser);

        Role userRole = new Role();
        userRole.setName("user");

        Subscription subscription = new Subscription();
        subscription.setRole(userRole);

        when(invitationRepository.findByIdAndInvitationStatus(1L, InvitationStatus.PENDING))
            .thenReturn(Optional.of(invitation));
        when(roleRepository.findByName("user")).thenReturn(Optional.of(userRole));
        when(subscriptionRepository.findById(any(SubscriptionKey.class))).thenReturn(Optional.of(subscription));

        assertThrows(RuntimeException.class, () -> invitationService.cancelInvitation(authUser, 1L));
    }

    @Test
    void cancelInvitation_ShouldThrowException_WhenUserHasLowerRoleIdThanInvitation() {
        User authUser = new User();
        authUser.setId(1L);

        Community community = new Community();
        community.setId(1L);

        Role userRole = new Role();
        userRole.setId(2L);  // The authUser's role ID

        Role invitationRole = new Role();
        invitationRole.setId(3L);  // The role ID required for the invitation, higher than user's role ID

        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setCommunity(community);
        invitation.setUser(authUser);
        invitation.setSentBy(authUser);  // authUser sent this invitation
        invitation.setInvitationStatus(InvitationStatus.PENDING);
        invitation.setRole(invitationRole);

        SubscriptionKey subsKey = new SubscriptionKey(authUser.getId(), community.getId());
        Subscription subscription = new Subscription();
        subscription.setRole(userRole);  // AuthUser's subscription role

        when(invitationRepository.findByIdAndInvitationStatus(1L, InvitationStatus.PENDING))
            .thenReturn(Optional.of(invitation));
        when(roleRepository.findByName("user")).thenReturn(Optional.of(userRole));
        when(subscriptionRepository.findById(subsKey)).thenReturn(Optional.of(subscription));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            invitationService.cancelInvitation(authUser, 1L)
        );

        assertEquals("You cannot cancel this invitation.", exception.getMessage());
    }

}
