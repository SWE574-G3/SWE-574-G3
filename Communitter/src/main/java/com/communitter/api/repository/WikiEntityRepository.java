package com.communitter.api.repository;

import com.communitter.api.model.User;
import com.communitter.api.model.UserInterest;
import com.communitter.api.model.WikiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface WikiEntityRepository extends JpaRepository<WikiEntity,String>{
    Optional<WikiEntity> findByCode(String code);
}
