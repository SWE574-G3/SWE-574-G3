package com.communitter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStreamDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long communityId;
    private String communityName;
    private Long postId;
//    private Long commentId;
//    private String commentContent;
    private String action;
    private LocalDateTime timestamp;
    private String description;
}
