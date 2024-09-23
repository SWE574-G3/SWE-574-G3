package com.communitter.api.community;

import com.communitter.api.user.Role;
import com.communitter.api.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="subscriptions")
public class Subscription {
    @EmbeddedId
    private SubscriptionKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-subs")
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @MapsId("communityId")
    @JoinColumn(name = "community_id")
    @JsonBackReference("community-subs")
    @EqualsAndHashCode.Exclude
    private Community community;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}

