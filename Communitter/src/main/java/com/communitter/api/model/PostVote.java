package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    @JsonBackReference("post-votes")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-votes")
    private User user;
}
