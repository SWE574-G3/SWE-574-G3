package com.communitter.api.repository;

import com.communitter.api.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostVoteRepository extends JpaRepository<PostVote, Long> {

    @Query("SELECT COUNT(v) FROM PostVote v WHERE v.isUpvote = :isUpvote AND v.post.id = :postId")
    long countVotesForPost(@Param("postId") Long postId, @Param("isUpvote") boolean isUpvote);
}
