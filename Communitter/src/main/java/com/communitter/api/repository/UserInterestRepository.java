package com.communitter.api.repository;

import com.communitter.api.model.User;
import com.communitter.api.model.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInterestRepository extends JpaRepository<UserInterest,Long> {

    Optional<UserInterest> findByUser(User user);

}
