package com.communitter.api.search;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchResultDto {

    private Long id;

    private String username;
}
