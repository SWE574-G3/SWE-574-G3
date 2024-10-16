package com.communitter.api.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunitySearchResultDto {
    private Long id;
    private String name;
}
