package com.communitter.api.model;

import com.communitter.api.key.SubscriptionKey;
import com.communitter.api.key.UserFollowKey;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "user_follow")
public class UserFollow {

    @EmbeddedId
    private UserFollowKey id;

    @ManyToOne
    @MapsId("follower_id")
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    private User follower;

    @ManyToOne
    @MapsId("followee_id")
    @JoinColumn(name = "followee_id", insertable = false, updatable = false)
    private User followee;

    @Column(name = "followed_at", nullable = false)
    private Date followedAt;
}
