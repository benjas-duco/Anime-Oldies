package com.benjamin.animeoldies.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="states")
public class States {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer state_id;
 
    @NotBlank
    private String name;
}