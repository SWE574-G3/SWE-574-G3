package com.communitter.api.service;

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
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final CommunityRepository communityRepository;
    private final InvitationRepository invitationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final InvitationMapper invitationMapper;

    public InvitationDto inviteUser(User authUser, InvitationCreateRequestDto request) {

        Community community = communityRepository.getReferenceById(request.getCommunityId());

        if (community.isPublic()) {
            throw new RuntimeException("Cannot invite a user for a public community");
        }

        User invitedUser = userRepository.findByUsername(request.getUsername()).orElseThrow();

        SubscriptionKey invitedSubKey = new SubscriptionKey(
            invitedUser.getId(), request.getCommunityId());

        Optional<Subscription> invitedSubscription = subscriptionRepository.findById(invitedSubKey);

        if (invitedSubscription.isPresent()) {
            throw new RuntimeException("User already subscribed");
        }

        Optional<Invitation> existingInvitation = invitationRepository.findByCommunityAndUserAndInvitationStatus(
            community, invitedUser, InvitationStatus.PENDING);

        if (existingInvitation.isPresent()) {
            throw new RuntimeException("User is already invited");
        }

        Role role = roleRepository.getReferenceById(request.getRoleId());

        Invitation invitation = Invitation.builder().user(invitedUser).community(community)
            .sentBy(authUser).role(role).sentAt(request.getSentAt()).build();

        Invitation sentInvitation = invitationRepository.save(invitation);

        return invitationMapper.toDto(sentInvitation);
    }

    public List<InvitationDto> getCommunityInvitations(Long communityId) {

        Community community = communityRepository.getReferenceById(communityId);

        List<Invitation> invitations = invitationRepository.findAllByCommunity(community);

        return invitations.stream().map(invitationMapper::toDto).toList();
    }

    public List<InvitationDto> getUserPendingInvitations(User user) {

        List<Invitation> invitations = invitationRepository.findAllByUserAndInvitationStatus(user,
            InvitationStatus.PENDING);

        return invitations.stream().map(invitationMapper::toDto).toList();
    }
}
