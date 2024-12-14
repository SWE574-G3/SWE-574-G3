package com.communitter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowInfoDto {

    private Long followerCount;

    private Long followeeCount;

    private Boolean followed;
}
