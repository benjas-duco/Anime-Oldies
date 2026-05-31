package com.benjamin.animeoldies.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateDTO {
    private Integer id;
    private Double score;
    private String body;
}