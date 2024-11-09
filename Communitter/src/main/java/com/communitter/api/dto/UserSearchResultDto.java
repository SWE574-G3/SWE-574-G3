package com.communitter.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchResultDto {

    private Long id;

    private String username;
}
