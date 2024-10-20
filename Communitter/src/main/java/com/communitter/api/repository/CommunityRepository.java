package com.communitter.api.repository;

import com.communitter.api.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community,Long> {

    Optional<Community> findByName(String name);

}
