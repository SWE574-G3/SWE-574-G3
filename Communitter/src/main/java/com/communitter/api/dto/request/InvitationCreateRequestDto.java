package com.communitter.api.dto.request;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationCreateRequestDto {
    private String username;

    private Long communityId;

    private Long roleId;

    private Date sentAt;
}