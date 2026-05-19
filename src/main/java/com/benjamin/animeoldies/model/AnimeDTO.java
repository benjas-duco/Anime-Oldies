package com.benjamin.animeoldies.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeDTO {
    private Integer id;
    private String title;
    private String resume;
    private double cultLevel;
    private String state;
    private List<String> links;
    private List<String> links_name;
}
