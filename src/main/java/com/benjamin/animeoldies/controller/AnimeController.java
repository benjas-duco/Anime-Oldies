package com.benjamin.animeoldies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.benjamin.animeoldies.model.Anime;
import com.benjamin.animeoldies.model.AnimeDTO;
import com.benjamin.animeoldies.model.Categoria;
import com.benjamin.animeoldies.model.Link;
import com.benjamin.animeoldies.model.Review;
import com.benjamin.animeoldies.service.AnimeService;

@RestController
@RequestMapping("/api/v1")
public class AnimeController {
    @Autowired
    private AnimeService service;

    @DeleteMapping
    public String deleteAnime(@RequestParam Integer animeId) {
        return service.borrarAnime(animeId);
    }

    @PutMapping
    public String updateAnime(@RequestBody Anime anime) {
        return service.editarAnime(anime);
    }

    @PostMapping
    public String addAnime(@RequestBody Anime anime) {
        return service.agregarAnime(anime);
    }

    @GetMapping("/anime/{animeId}")
    public Anime getAnimeById(@PathVariable Integer animeId) {
        return service.obtenerAnimesPorId(animeId);
    }

    @GetMapping
    public List<Anime> getAnimes() {
        return service.obtenerAnimes();
    }

    @GetMapping("/state")
    public String getState(@RequestParam Integer animeId) {
        return service.obtenerEstado(animeId);
    }

    @GetMapping("/reviews")
    public List<Review> obtenerReviews(@RequestParam Integer animeId, @RequestParam String state) {
        return service.obtenerReviews(animeId, state);
    }

    @GetMapping("/cult")
    public double getCultLevel(@RequestParam Integer animeId) {
        return service.obtenerCultLevel(animeId);
    }

    @GetMapping("/getcat")
    public List<Categoria> getAnimeCategories(@RequestParam Integer animeId) {
        return service.obtenerCategorias(animeId);
    }

    @GetMapping("/links")
    public List<Link> getLinks(@RequestParam Integer animeId) {
        return service.obtenerLinks(animeId);
    }

    @GetMapping("/animescat")
    public List<AnimeDTO> getAnimeByCategory(@RequestParam Integer categoryId) {
        return service.obtenerAnimesPorCategoria(categoryId);
    }
}