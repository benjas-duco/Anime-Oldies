package com.benjamin.animeoldies.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="anime_categories")
public class CategoriaAnime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categoria category;
}
