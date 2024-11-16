package com.communitter.api.repository;

import com.communitter.api.model.Community;
import com.communitter.api.model.CommunityLabel;
import com.communitter.api.model.WikiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CommunityLabelRepository extends JpaRepository<CommunityLabel,Long> {

    Optional<CommunityLabel> findByCommunity(Community community);
    Optional<CommunityLabel> findByWikiEntity(WikiEntity wikiEntity);
    Optional<Set<CommunityLabel>> findAllByWikiEntityIn(Set<WikiEntity> wikiEntities);
    Optional<CommunityLabel> findAllByCommunity(Community community);
    Optional<CommunityLabel> findByCommunityAndWikiEntity(Community community, WikiEntity wikiEntity);

}
