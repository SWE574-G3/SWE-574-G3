package com.communitter.api.service;

import com.communitter.api.dto.InvitationDto;
import com.communitter.api.dto.request.InvitationCreateRequestDto;
import com.communitter.api.key.SubscriptionKey;
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
            .sentBy(authUser).userCommunityRole(role).sentAt(request.getSentAt()).build();

        Invitation sentInvitation = invitationRepository.save(invitation);

        return new InvitationDto(sentInvitation.getId(), sentInvitation.getUser().getUsername(),
            sentInvitation.getCommunity().getId(), sentInvitation.getUserCommunityRole().getId(),
            sentInvitation.getInvitationStatus(), sentInvitation.getSentBy().getId(),
            sentInvitation.getSentAt());
    }
}
