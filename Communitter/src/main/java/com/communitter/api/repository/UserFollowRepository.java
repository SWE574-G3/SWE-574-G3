package com.communitter.api.repository;

import com.communitter.api.model.User;
import com.communitter.api.model.UserFollow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    List<UserFollow> findAllByFolloweeId(Long followeeId);

    List<UserFollow> findAllByFollowerId(Long followerId);

    Optional<UserFollow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Long countByFolloweeId(Long followeeId);

    Long countByFollowerId(Long followerId);

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
