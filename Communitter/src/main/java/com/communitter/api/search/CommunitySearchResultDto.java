package com.communitter.api.search;


import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunitySearchResultDto {
    private Long id;
    private String name;
}
