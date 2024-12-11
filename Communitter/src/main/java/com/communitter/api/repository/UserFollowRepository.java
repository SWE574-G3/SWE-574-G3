package com.communitter.api.repository;

import com.communitter.api.key.UserFollowKey;
import com.communitter.api.model.User;
import com.communitter.api.model.UserFollow;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowKey> {

    List<UserFollow> findAllByFollowee(User followee);

    List<UserFollow> findAllByFollower(User follower);
}
