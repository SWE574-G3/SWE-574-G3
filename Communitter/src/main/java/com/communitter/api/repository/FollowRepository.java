package com.communitter.api.repository;

import com.communitter.api.model.Follow;
import com.communitter.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

    boolean existsByFollowerAndFollowed(User follower, User followed);

    void deleteByFollowerAndFollowed(User follower, User followed);

}
