package com.communitter.api.repository;

import com.communitter.api.model.User;
import com.communitter.api.model.UserInterest;
import com.communitter.api.model.WikiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserInterestRepository extends JpaRepository<UserInterest,Long> {

    Optional<UserInterest> findByUser(User user);
    Optional<Set<UserInterest>> findAllByUser(User user);
    Optional<UserInterest> findByWikiEntity(WikiEntity wikiEntity);
    Optional<UserInterest> findByUserAndWikiEntity(User user, WikiEntity wikiEntity);

}
