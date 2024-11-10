package com.communitter.api.model;

import com.communitter.api.key.PostVoteKey;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="post_votes")
public class PostVote {
    @EmbeddedId
    private PostVoteKey id;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "is_upvote", nullable = false)
    private boolean isUpvote;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference("post-votes")
    @EqualsAndHashCode.Exclude
    private Post post;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-votes")
    @EqualsAndHashCode.Exclude
    private User user;
}
