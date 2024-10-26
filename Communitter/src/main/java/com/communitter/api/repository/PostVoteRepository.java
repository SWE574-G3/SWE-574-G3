package com.communitter.api.repository;

import com.communitter.api.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostVoteRepository extends JpaRepository<PostVote,Long> {
}
