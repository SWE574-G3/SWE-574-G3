package com.communitter.api.dto;

import com.communitter.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {

    private Long id;

    private String name;

    private String about;

    private boolean isPublic;

    private UserDto creator;
}
