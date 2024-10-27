package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="post_vote")
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

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime createdAt;

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

    public PostVote(Long id, boolean isUpvote, Post postToVote, User user) {
        this.id = id;
        this.isUpvote = isUpvote;
        this.post = postToVote;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
