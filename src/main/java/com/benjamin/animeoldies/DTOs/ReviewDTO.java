package com.benjamin.animeoldies.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer id;
    private Integer userId;
    private Integer animeId;
    private Double score;
    private String body;
    private String state;
}
