package com.communitter.api.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowKey implements Serializable {

    @Column(name = "follower_id")
    private Long followerId;

    @Column(name = "followee_id")
    private Long followeeId;
}
