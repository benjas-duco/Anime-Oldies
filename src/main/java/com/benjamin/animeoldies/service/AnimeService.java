package com.benjamin.animeoldies.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benjamin.animeoldies.model.Anime;
import com.benjamin.animeoldies.model.AnimeDTO;
import com.benjamin.animeoldies.model.Categoria;
import com.benjamin.animeoldies.model.Link;
import com.benjamin.animeoldies.model.Review;
import com.benjamin.animeoldies.model.State;
import com.benjamin.animeoldies.repository.AnimeRepo;
import com.benjamin.animeoldies.repository.CategoriaAnimeRepo;
import com.benjamin.animeoldies.repository.LinkRepo;
import com.benjamin.animeoldies.repository.ReviewRepo;

@Service
public class AnimeService {
    @Autowired
    AnimeRepo animeRepo;

    @Autowired
    LinkRepo linkRepo;

    @Autowired
    CategoriaAnimeRepo categoriaAnimeRepo;

    @Autowired
    ReviewRepo reviewRepo;

    public String obtenerEstado(Integer animeId) {
        Optional<Anime> anime = animeRepo.findById(animeId);
        State estado = anime.get().getState();
        return estado.getName();
    }

    public List<Review> obtenerReviews(Integer animeId, String state) {
        List<Review> reviews = reviewRepo.findByAnime_Id(animeId);
        List<Review> final_list = new ArrayList<>();

        for(Review rev : reviews) {
            if(rev.getState().getName().equals(state)) {
                final_list.add(rev);
            }
        }

        return final_list;
    }

    public double obtenerCultLevel(Integer animeId) {
        List<Review> reviews = obtenerReviews(animeId, "aprobado");
        double reviewCount = reviews.size();
        double scoreSum = 0;

        for(Review rev : reviews) {
            scoreSum += rev.getScore();
        }

        if(reviewCount == 0) return 0;

        double averageScore = scoreSum / reviewCount;

        return averageScore / Math.log(reviewCount + 1);
    }

    public List<Categoria> obtenerCategorias(Integer animeId) {
        return categoriaAnimeRepo.findCategoryByAnimeId(animeId);
    }

    public List<Link> obtenerLinks(Integer animeId) {
        return linkRepo.findLinkByAnimeId(animeId);
    }

    public List<AnimeDTO> obtenerAnimesPorCategoria(Integer categoryId) {
        List<Anime> animes = categoriaAnimeRepo.findAnimeByCategoryId(categoryId);
        List<AnimeDTO> listaAnimeDTOs = new ArrayList<>();

        for(Anime anime : animes) {
            AnimeDTO dto = new AnimeDTO();

            dto.setId(anime.getId());
            dto.setTitle(anime.getTitle());
            dto.setResume(anime.getResume());

            List<String> linkNames = new ArrayList<>();
            List<String> linkLinks = new ArrayList<>();

            List<Link> links = obtenerLinks(anime.getId());
            for(Link link : links) {
                linkNames.add(link.getName());
                linkLinks.add(link.getLink());
            }

            dto.setLinks(linkLinks);
            dto.setLinks_name(linkNames);

            dto.setState(obtenerEstado(anime.getId()));
            dto.setCultLevel(obtenerCultLevel(anime.getId()));

            listaAnimeDTOs.add(dto);
        }

        return listaAnimeDTOs;
    }
}
