package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ImageData")
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String mimeType;
    
    
    private byte[] data_image;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true) // Foreign key in ImageData
    @JsonBackReference("user-profile-picture")
    @ToString.Exclude // Avoid circular references
    @EqualsAndHashCode.Exclude
    private User user;

    

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = true)
    @JsonBackReference("community-subs")
    private Community community;

}
