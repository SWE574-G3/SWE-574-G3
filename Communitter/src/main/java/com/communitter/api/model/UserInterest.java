package com.communitter.api.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_interests",indexes = {@Index(name = "idx_user_id", columnList = "user_id"),@Index(name = "idx_wiki_entity_code", columnList = "wiki_entity_code")},uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "wiki_entity_code" }) })
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"avatar","about","subscriptions","email","password","header","posts","createdCommunities"})
    private User user;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "wiki_entity_code", nullable = false)
    private WikiEntity wikiEntity;
}
