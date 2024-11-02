package com.communitter.api.controller;

import com.communitter.api.dto.InvitationDto;
import com.communitter.api.dto.request.InvitationCreateRequestDto;
import com.communitter.api.model.User;
import com.communitter.api.service.InvitationService;
import com.communitter.api.util.InvitationStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/invitation")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PreAuthorize("@authorizer.checkSubscription(#root,#request.communityId)")
    @PostMapping
    public ResponseEntity<InvitationDto> sendInvitation(@AuthenticationPrincipal User authUser,
        @RequestBody InvitationCreateRequestDto request) {
        return ResponseEntity.ok(invitationService.inviteUser(authUser, request));
    }

    @PreAuthorize("@authorizer.checkSubscription(#root,#communityId)")
    @GetMapping(params = {"communityId"})
    public ResponseEntity<List<InvitationDto>> getCommunityInvitations(
        @RequestParam("communityId") Long communityId) {
        return ResponseEntity.ok(invitationService.getCommunityInvitations(communityId));
    }

    @GetMapping
    public ResponseEntity<List<InvitationDto>> getUserInvitations(
        @AuthenticationPrincipal User authUser) {
        return ResponseEntity.ok(invitationService.getUserPendingInvitations(authUser));
    }

    @PutMapping("/{invitationId}/cancel-invitation")
    public ResponseEntity<InvitationDto> cancelInvitation(@AuthenticationPrincipal User authUser,
        @PathVariable("invitationId") Long invitationId) {
        return ResponseEntity.ok(invitationService.cancelInvitation(authUser, invitationId));
    }

    @PutMapping("/{invitationId}/reject-invitation")
    public ResponseEntity<InvitationDto> rejectInvitation(@AuthenticationPrincipal User authUser,
        @PathVariable("invitationId") Long invitationId) {
        return ResponseEntity.ok(invitationService.acceptOrRejectInvitation(authUser, invitationId,
            InvitationStatus.REJECTED));
    }

    @PutMapping("/{invitationId}/accept-invitation")
    public ResponseEntity<InvitationDto> acceptInvitation(@AuthenticationPrincipal User authUser,
        @PathVariable("invitationId") Long invitationId) {
        return ResponseEntity.ok(invitationService.acceptOrRejectInvitation(authUser, invitationId,
            InvitationStatus.ACCEPTED));
    }
}