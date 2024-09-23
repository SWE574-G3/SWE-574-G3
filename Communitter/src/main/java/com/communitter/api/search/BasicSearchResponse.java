package com.communitter.api.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicSearchResponse {
    private List<CommunitySearchResultDto> communities;
    private List<UserSearchResultDto> users;
}
