package com.communitter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInCommunityDto {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String roleName;
}
