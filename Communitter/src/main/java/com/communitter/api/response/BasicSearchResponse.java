package com.communitter.api.response;

import com.communitter.api.dto.CommunitySearchResultDto;
import com.communitter.api.dto.UserSearchResultDto;
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
