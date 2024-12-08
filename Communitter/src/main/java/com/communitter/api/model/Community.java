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
@Table(name="communities")
public class Community {
    @Id
    @SequenceGenerator(
            name="community_sequence",
            sequenceName = "community_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "community_sequence")
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;
    private String about;

    @Column(name = "is_public",nullable = false)
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonBackReference("community-creator")
    @JsonIgnore
    private User creator;

    @OneToMany(mappedBy = "community",fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    @JsonManagedReference("community-subs")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Subscription> subscriptions;

    @OneToMany(mappedBy = "community",fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    @JsonManagedReference("community-templates")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Template> templates;

    @OneToMany(mappedBy = "community",fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    @JsonManagedReference("community-posts")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnoreProperties({"community"})
    private Set<Post> posts;



    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "community_image_id") // Specifies the foreign key in User
    @JsonManagedReference("community-profile-picture")
    @ToString.Exclude // Avoid circular references
    @EqualsAndHashCode.Exclude
    private ImageData communityImage;




    @OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference("community-labels")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<CommunityLabel> labels;

}
