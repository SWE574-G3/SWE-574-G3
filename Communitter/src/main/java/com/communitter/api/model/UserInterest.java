package com.communitter.api.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-interests")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    //@JsonIgnoreProperties({"posts","subscriptions","interests"})
    private User user;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "wiki_entity_code", nullable = false)
    private WikiEntity wikiEntity;
}
