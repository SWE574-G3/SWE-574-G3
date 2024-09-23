package com.communitter.api.templates;

import com.communitter.api.community.Community;
import com.communitter.api.community.Subscription;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="templates",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "community_id" }) })
public class Template {
    @Id
    @SequenceGenerator(
            name="template_sequence",
            sequenceName = "template_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "template_sequence")
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "template",fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference("template-datafields")
    private Set<DataField> dataFields;

    @ManyToOne
    @JoinColumn(name = "community_id")
    @JsonBackReference("community-templates")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Community community;
}
