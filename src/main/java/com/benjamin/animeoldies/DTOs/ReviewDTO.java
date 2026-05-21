package com.benjamin.animeoldies.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer userId;
    private Integer animeId;
    private Integer score;
    private String body;
}
