package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="wiki_entities")
public class WikiEntity {
    @Id
    private String code;
    @Column(nullable = false)
    private String label;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "parent_lookup",
            joinColumns = @JoinColumn(name = "entity_code")
    )
    @Column(name = "parent_code")
    private Set<String> parentCodes;

}
