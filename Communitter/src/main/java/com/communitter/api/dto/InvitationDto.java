package com.communitter.api.dto;

import com.communitter.api.model.User;
import com.communitter.api.util.InvitationStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDto {

    private Long id;

    private UserDto user;

    private CommunityDto community;

    private RoleDto role;

    private InvitationStatus invitationStatus;

    private UserDto sentBy;

    private Date sentAt;
}
