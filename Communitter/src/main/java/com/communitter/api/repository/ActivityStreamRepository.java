package com.communitter.api.repository;

import com.communitter.api.model.ActivityStream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityStreamRepository extends JpaRepository<ActivityStream, Long> {
}
