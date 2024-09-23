package com.communitter.api.community;

import com.communitter.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription,SubscriptionKey> {

    Optional<Subscription> findByUser(User user);

    Optional<Subscription> findByCommunity(Community community);

}
