package com.communitter.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="data_field_enum_values")
public class DataFieldEnumValue {
    @Id
    @SequenceGenerator(
            name="data_field_enum_value_sequence",
            sequenceName = "data_field_enum_value_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "data_field_enum_value_sequence")
    private Long id;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "data_field_id")
    @JsonBackReference("data-field-enums")
    @EqualsAndHashCode.Exclude
    private DataField dataField;
}
