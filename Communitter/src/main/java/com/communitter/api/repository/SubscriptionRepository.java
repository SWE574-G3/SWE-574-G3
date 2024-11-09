package com.communitter.api.repository;

import com.communitter.api.model.Community;
import com.communitter.api.model.Subscription;
import com.communitter.api.key.SubscriptionKey;
import com.communitter.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionKey> {

    Optional<Subscription> findByUser(User user);

    Optional<Subscription> findByCommunity(Community community);

}
