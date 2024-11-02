package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="post_votes")
public class PostVote {
    @Id
    @SequenceGenerator(
            name="post_vote_sequence",
            sequenceName = "post_vote_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "post_vote_sequence")
    private Long id;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "is_upvote", nullable = false)
    private boolean isUpvote;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference("post-votes")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-votes")
    private User user;
}
