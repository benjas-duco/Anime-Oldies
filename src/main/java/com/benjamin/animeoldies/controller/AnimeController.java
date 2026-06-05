package com.benjamin.animeoldies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.benjamin.animeoldies.DTOs.CategoriaDTO;
import com.benjamin.animeoldies.DTOs.ContentBody;
import com.benjamin.animeoldies.DTOs.LinkDTO;
import com.benjamin.animeoldies.DTOs.ReviewDTO;
import com.benjamin.animeoldies.DTOs.AnimeAddDTO;
import com.benjamin.animeoldies.model.State;
import com.benjamin.animeoldies.service.AnimeService;

@RestController
@RequestMapping("/api/v1")
public class AnimeController {
    @Autowired
    private AnimeService animeService;

    @PostMapping("anime/{animeId}/add-category")
    public ResponseEntity<String> addCategoryToAnime(@PathVariable Integer animeId, @RequestParam String name){
        return animeService.agregarAnimeCategoria(animeId, name);
    }

    @DeleteMapping("category/{cateId}")
    public ResponseEntity<String> eliminarAnimeCategoria(@PathVariable Integer cateId) {
        return animeService.eliminarAnimeCategoria(cateId);
    }

    @PutMapping("category")
    public ResponseEntity<String> editarAnimeCategoria(@RequestBody CategoriaDTO cate) {
        return animeService.editarAnimeCategoria(cate);
    }

    @DeleteMapping("link/{linkId}")
    public ResponseEntity<String> eliminarAnimeLink(@PathVariable Integer linkId) {
        return animeService.eliminarAnimeLink(linkId);
    }

    @PutMapping("link")
    public ResponseEntity<String> editarAnimeLink(@RequestBody LinkDTO newlnk) {
        return animeService.editarAnimeLink(newlnk);
    }

    @PostMapping("anime/{animeId}/add-link")
    public ResponseEntity<String> agregarAnimeLink(@PathVariable Integer animeId, @RequestBody LinkDTO link) {
        return animeService.agregarAnimeLink(animeId, link);
    }

    @PutMapping("anime/{animeId}/resume")
    public ResponseEntity<String> actualizarAnimeResumen(@PathVariable Integer animeId, @RequestBody ContentBody content) {
        return animeService.actualizarAnimeResumen(animeId, content);
    }

    @PutMapping("anime/{animeId}/title")
    public ResponseEntity<String> actualizarAnimeNombre(@PathVariable Integer animeId, @RequestParam String nombre) {
        return animeService.actualizarAnimeNombre(animeId, nombre);
    }

    @DeleteMapping("/anime/{animeId}")
    public ResponseEntity<String> deleteAnime(@PathVariable Integer animeId) {
        return animeService.borrarAnime(animeId);
    }

    @PostMapping("/anime")
    public ResponseEntity<String> postAnime(@RequestBody AnimeAddDTO anime) {
        return animeService.agregarAnime(anime);
    }

    @GetMapping("/anime/{animeId}/state")
    public State getState(@PathVariable Integer animeId) {
        return animeService.obtenerEstado(animeId);
    }

    @GetMapping("/anime/{animeId}/reviews")
    public List<ReviewDTO> getReviews(@PathVariable Integer animeId, @RequestParam String state) {
        return animeService.obtenerReviews(animeId, state);
    }

    @GetMapping("/anime/{animeId}/categories")
    public List<CategoriaDTO> getCategories(@PathVariable Integer animeId) {
        return animeService.obtenerCategorias(animeId);
    }

    @GetMapping("/anime/{animeId}/links")
    public List<LinkDTO> getLinks(@PathVariable Integer animeId) {
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
    public ResponseEntity<String> aproveAnime(@RequestParam String passwd, @PathVariable Integer animeId) {
        return animeService.aprobarAnime(passwd, animeId);
    }

    @PutMapping("/anime/{animeId}/decline")
    public ResponseEntity<String> declineAnime(@RequestParam String passwd, @PathVariable Integer animeId) {
        return animeService.rechazarAnime(passwd, animeId);
    }

    @PutMapping("/anime/{animeId}/reset")
    public ResponseEntity<String> resetAnime(@RequestParam String passwd, @PathVariable Integer animeId) {
        return animeService.resetearAnime(passwd, animeId);
    }
}