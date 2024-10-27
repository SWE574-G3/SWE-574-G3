package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="posts")
public class Post {
    @Id
    @SequenceGenerator(
            name="post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "post_sequence")
    private Long id;
    @Column(nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id",nullable = false)
    @JsonIgnoreProperties({"avatar","about","subscriptions"})
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "community_id",nullable = false)
    @JsonBackReference("community-posts")
    @JsonIgnoreProperties({"subscriptions","templates","posts","authorities","accountNonExpired","accountNonLocked","credentialsNonExpired"})
    private Community community;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id",nullable = false)
    @JsonIgnoreProperties({"dataFields","community"})
    private Template template;

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    @JsonManagedReference("post-fields")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PostField> postFields;
}