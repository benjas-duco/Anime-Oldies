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

import com.benjamin.animeoldies.DTOs.AnimeDTO;
import com.benjamin.animeoldies.DTOs.AnimeUpdateDTO;
import com.benjamin.animeoldies.model.Categoria;
import com.benjamin.animeoldies.model.Link;
import com.benjamin.animeoldies.model.Review;
import com.benjamin.animeoldies.service.AnimeService;

@RestController
@RequestMapping("/api/v1")
public class AnimeController {
    @Autowired
    private AnimeService animeService;

    @DeleteMapping("/anime/{animeId}")
    public String deleteAnime(@PathVariable Integer animeId) {
        return animeService.borrarAnime(animeId);
    }

    @PutMapping("/anime/{animeId}")
    public String updateAnime(@PathVariable Integer animeId, @RequestBody AnimeUpdateDTO anime) {
        return animeService.editarAnime(animeId, anime);
    }

    @PostMapping("/anime")
    public String postAnime(@RequestBody AnimeUpdateDTO anime) {
        return animeService.agregarAnime(anime);
    }

    @GetMapping("/anime/{animeId}/state")
    public String getState(@PathVariable Integer animeId) {
        return animeService.obtenerEstado(animeId);
    }

    @GetMapping("/anime/{animeId}/reviews")
    public List<Review> getReviews(@PathVariable Integer animeId, @RequestParam String state) {
        return animeService.obtenerReviews(animeId, state);
    }

    @GetMapping("/anime/{animeId}/categories")
    public List<Categoria> getCategories(@PathVariable Integer animeId) {
        return animeService.obtenerCategorias(animeId);
    }

    @GetMapping("/anime/{animeId}/links")
    public List<Link> getLinks(@PathVariable Integer animeId) {
        return animeService.obtenerLinks(animeId);
    }

    @GetMapping("/anime")
    public List<AnimeDTO> getAnimes() {
        return animeService.obtenerAnimes();
    }

    @GetMapping("/anime/by-category/{categoryId}")
    public List<AnimeDTO> getAnimesByCategory(@PathVariable Integer categoryId) {
        return animeService.obtenerAnimesPorCategoria(categoryId);
    }

    @GetMapping("/anime/by-state/{state}")
    public List<AnimeDTO> getAnimesByState(@PathVariable String state) {
        return animeService.obtenerAnimesPorEstado(state);
    }

    @GetMapping("/anime/{animeId}")
    public AnimeDTO getAnimesById(@PathVariable Integer animeId) {
        return animeService.obtenerAnimesPorId(animeId);
    }

    @PutMapping("/anime/{animeId}/approve")
    public String aproveAnime(@RequestParam String passwd, @PathVariable Integer animeId) {
        return animeService.aprobarAnime(passwd, animeId);
    }

    @PutMapping("/anime/{animeId}/decline")
    public String declineAnime(@RequestParam String passwd, @PathVariable Integer animeId) {
        return animeService.rechazarAnime(passwd, animeId);
    }

    @PutMapping("/anime/{animeId}/reset")
    public String resetAnime(@RequestParam String passwd, @PathVariable Integer animeId) {
        return animeService.resetearAnime(passwd, animeId);
    }
}