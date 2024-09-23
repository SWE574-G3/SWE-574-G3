package com.communitter.api.templates;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="data_field_type")
public class DataFieldType {
    @Id
    @SequenceGenerator(
            name="data_field_type_sequence",
            sequenceName = "data_field_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "data_field_type_sequence")
    private Long id;
    @Column(nullable = false)
    private String type;
}
