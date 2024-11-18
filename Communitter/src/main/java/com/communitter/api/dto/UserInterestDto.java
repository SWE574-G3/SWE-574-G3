package com.communitter.api.dto;

import com.communitter.api.model.User;
import com.communitter.api.model.WikiEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInterestDto {
    private Long id;
    private Long userId;
    private WikiEntity wikiEntity;
}
