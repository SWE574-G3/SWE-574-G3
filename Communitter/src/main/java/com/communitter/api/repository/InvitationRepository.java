package com.communitter.api.repository;

import com.communitter.api.model.Community;
import com.communitter.api.model.Invitation;
import com.communitter.api.model.User;
import com.communitter.api.util.InvitationStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByCommunityAndUserAndInvitationStatus(Community community,
        User user,
        InvitationStatus invitationStatus);
}
