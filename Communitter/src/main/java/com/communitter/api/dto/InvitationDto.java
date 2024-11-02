package com.communitter.api.dto;

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

    private String username;

    private  Long communityId;

    private Long roleId;

    private InvitationStatus invitationStatus;

    private Long sentBy;

    private Date sentAt;
}
