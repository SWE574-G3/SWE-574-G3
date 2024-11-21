package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "community_id", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference("community-labels")
    //@JsonIgnoreProperties({"posts","subscriptions","templates","creator","labels"})
    private Community community;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "wiki_entity_code", nullable = false)
    private WikiEntity wikiEntity;
}
