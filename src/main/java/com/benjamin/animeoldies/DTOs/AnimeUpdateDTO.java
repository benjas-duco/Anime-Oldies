package com.benjamin.animeoldies.DTOs;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeUpdateDTO {
    private String title;
    private String resume;
    private List<LinkDTO> links = new ArrayList<>();
    private List<CategoriaDTO> categories = new ArrayList<>();
}
