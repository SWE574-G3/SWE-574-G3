package com.communitter.api.dto;
import com.communitter.api.model.Community;
import com.communitter.api.model.WikiEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityLabelDto {
    private Long id;
    private Long communityId;
    private WikiEntity wikiEntity;
}
