package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="community_labels",indexes = {@Index(name = "idx_community_id", columnList = "community_id"),@Index(name = "idx_wiki_entity_code", columnList = "wiki_entity_code")},uniqueConstraints = { @UniqueConstraint(columnNames = { "community_id", "wiki_entity_code" }) })
public class CommunityLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    @JsonIgnoreProperties({"subscriptions","templates","posts"})
    private Community community;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "wiki_entity_code", nullable = false)
    private WikiEntity wikiEntity;
}
