package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="activity_stream")
public class ActivityStream {

    @Id
    @SequenceGenerator(
            name="activity_stream_sequence",
            sequenceName = "activity_stream_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "activity_stream_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"posts","labels","creator","subscriptions"})
    @JoinColumn(name = "community_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Community community;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnoreProperties({"avatar","about","subscriptions","interests","posts","createdCommunities","accountNonExpired","accountNonLocked","credentialsNonExpired","authorities"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    @JsonIgnoreProperties({"community","activityStreams","author","comments","postFields","template","postVotes"})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Post post;
}
