package com.communitter.api.repository;

import com.communitter.api.model.PostField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFieldRepository  extends JpaRepository<PostField,Long> {
}
