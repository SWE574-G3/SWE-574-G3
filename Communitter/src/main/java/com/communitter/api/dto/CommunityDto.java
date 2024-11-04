package com.communitter.api.dto;

import com.communitter.api.model.Post;
import com.communitter.api.model.Subscription;
import com.communitter.api.model.Template;
import com.communitter.api.model.User;
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
@Builder
public class CommunityDto {

    private Long id;

    private String name;
    private String about;


    private boolean isPublic;


    private User creator;


}
