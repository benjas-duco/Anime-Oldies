package com.benjamin.animeoldies.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.benjamin.animeoldies.DTOs.AnimeDTO;
import com.benjamin.animeoldies.DTOs.AnimeUpdateDTO;
import com.benjamin.animeoldies.DTOs.CategoriaDTO;
import com.benjamin.animeoldies.DTOs.LinkDTO;
import com.benjamin.animeoldies.model.Anime;
import com.benjamin.animeoldies.model.Categoria;
import com.benjamin.animeoldies.model.CategoriaAnime;
import com.benjamin.animeoldies.model.Link;
import com.benjamin.animeoldies.model.Review;
import com.benjamin.animeoldies.model.State;
import com.benjamin.animeoldies.repository.AnimeRepo;
import com.benjamin.animeoldies.repository.CategoriaAnimeRepo;
import com.benjamin.animeoldies.repository.CategoriaRepo;
import com.benjamin.animeoldies.repository.LinkRepo;
import com.benjamin.animeoldies.repository.ReviewRepo;
import com.benjamin.animeoldies.repository.StateRepo;

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

    @Autowired
    StateRepo stateRepo;

    @Autowired
    CategoriaRepo categoriaRepo;

    private int testLinks(List<LinkDTO> links) {
        for(LinkDTO l : links) {
            if (l.getLink() == null || l.getLink().strip().equals("")) return 1;
            if (l.getName() == null || l.getName().strip().equals("")) return 2;
        }
        return 0;
    }

    private void agregarLinks(Integer animeId, List<LinkDTO> links) {
        for(LinkDTO l : links) {
            Link link = new Link();

            link.setAnime(animeRepo.findById(animeId).get());
            link.setLink(l.getLink());
            link.setName(l.getName());

            linkRepo.save(link);
        }
    }

    private void borrarLinks(Integer animeId) {
        linkRepo.deleteByAnime_Id(animeId);
    }

    private int testCategories(List<CategoriaDTO> cats) {
        for(CategoriaDTO c : cats) {
            if (c.getName() == null || c.getName().strip().equals("")) return 1;
        }
        return 0;
    }

    private void agregarCategorias(Integer animeId, List<CategoriaDTO> cats) {
        Optional<Anime> anime = animeRepo.findById(animeId);
        for(CategoriaDTO c : cats) {
            Optional<Categoria> cat = categoriaRepo.findByName(c.getName());
            CategoriaAnime catan = new CategoriaAnime();

            if(cat.isEmpty()) {
                Categoria ca = new Categoria();
                ca.setName(c.getName());
                categoriaRepo.save(ca);

                catan.setCategory(ca);
            }else{
                catan.setCategory(cat.get());
            }

            catan.setAnime(anime.get());
            categoriaAnimeRepo.save(catan);
        }
    }

    private void borrarCategorias(Integer animeID) {
        categoriaAnimeRepo.deleteByAnime_Id(animeID);
    }

    public ResponseEntity<String> borrarAnime(Integer animeId) {
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Anime> an = animeRepo.findById(animeId);
        if(an.isEmpty()) return ResponseEntity.status(404).body("Anime no encontrado");

        reviewRepo.deleteByAnime_Id(animeId);
        categoriaAnimeRepo.deleteByAnime_Id(animeId);
        linkRepo.deleteByAnime_Id(animeId);
        animeRepo.deleteById(animeId);
        return ResponseEntity.ok("Anime eliminado correctamente");
    }

    public ResponseEntity<String> editarAnime(Integer animeId, AnimeUpdateDTO anime) {
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Anime> an = animeRepo.findById(animeId);
        if(an.isEmpty()) return ResponseEntity.status(404).body("Anime no encontrado");

        if (anime.getTitle() == null || anime.getTitle().strip().equals("")) 
            return ResponseEntity.badRequest().body("El titulo del anime no puede ser nulo o estar en blanco");
        if (anime.getResume() == null || anime.getResume().strip().equals("")) 
            return ResponseEntity.badRequest().body("El resumen del anime no puede ser nulo o estar en blanco");
        if (anime.getCategories() == null || anime.getCategories().isEmpty()) 
            return ResponseEntity.badRequest().body("Este anime no puede no tener ninguna categoria asociada");

        if (anime.getLinks() != null) {
            int rel = testLinks(anime.getLinks());

            if(rel == 1) return ResponseEntity.badRequest().body("No se puede actualizar el anime con un link vacio o nulo");
            if(rel == 2) return ResponseEntity.badRequest().body("No se puede actualizar el anime con un link sin nombre o nulo");

            borrarLinks(animeId);
            agregarLinks(animeId,anime.getLinks());
        }

        int rel = testCategories(anime.getCategories());

        if(rel == 1) return ResponseEntity.badRequest().body("No se puede actualizar el anime con una categoria vacia o nula");

        borrarCategorias(animeId);
        agregarCategorias(animeId,anime.getCategories());

        Anime finalAnime = an.get();

        finalAnime.setTitle(anime.getTitle());
        finalAnime.setResume(anime.getResume());

        animeRepo.save(finalAnime);

        return ResponseEntity.ok("Anime actualizado correctamente");
    }

    public ResponseEntity<String> agregarAnime(AnimeUpdateDTO anime) {
        if (anime.getTitle() == null || anime.getTitle().strip().equals("")) 
            return ResponseEntity.badRequest().body("El titulo del anime no puede ser nulo o estar en blanco");
        if (anime.getResume() == null || anime.getResume().strip().equals("")) 
            return ResponseEntity.badRequest().body("El resumen del anime no puede ser nulo o estar en blanco");
        if (anime.getCategories() == null || anime.getCategories().isEmpty()) 
            return ResponseEntity.badRequest().body("Este anime no puede no tener ninguna categoria asociada");

        Optional<Anime> an = animeRepo.findByTitle(anime.getTitle());
        if(!an.isEmpty()) return ResponseEntity.status(409).body("Ya existe un Anime con este titulo");

        Anime a = new Anime();

        if (anime.getLinks() != null) {
            int rel = testLinks(anime.getLinks());
            if(rel == 1) return ResponseEntity.badRequest().body("No se puede actualizar el anime con un link vacio o nulo");
            if(rel == 2) return ResponseEntity.badRequest().body("No se puede actualizar el anime con un link sin nombre o nulo");
        }

        int rel = testCategories(anime.getCategories());

        if(rel == 1) return ResponseEntity.badRequest().body("No se puede actualizar el anime con una categoria vacia o nula");

        a.setTitle(anime.getTitle());
        a.setResume(anime.getResume());
        a.setState(stateRepo.findByName("en revision").get());

        Anime saved = animeRepo.save(a);

        if (anime.getLinks() != null) agregarLinks(saved.getId(),anime.getLinks());
        agregarCategorias(saved.getId(),anime.getCategories());

        return ResponseEntity.ok("Anime agregado correctamente");
    }

    public State obtenerEstado(Integer animeId) {
        if(animeId == null) return new State();
        Optional<Anime> anime = animeRepo.findById(animeId);
        if(anime.isEmpty()) return new State();

        State estado = anime.get().getState();
        return estado;
    }

    public List<Review> obtenerReviews(Integer animeId, String state) {
        if(animeId == null) return new ArrayList<>();
        List<Review> reviews = reviewRepo.findByAnime_Id(animeId);
        List<Review> final_list = new ArrayList<>();

        for(Review rev : reviews) {
            if(rev.getState().getName().equals(state)) {
                final_list.add(rev);
            }
        }

        return final_list;
    }

    private double calcularCultLevel(Integer animeId) {
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
        if(animeId == null) return new ArrayList<>();
        return categoriaAnimeRepo.findCategoryByAnimeId(animeId);
    }

    public List<Link> obtenerLinks(Integer animeId) {
        if(animeId == null) return new ArrayList<>();
        return linkRepo.findLinkByAnimeId(animeId);
    }

    private AnimeDTO animeToDTO(Anime anime) {
        AnimeDTO dto = new AnimeDTO();

        List<Link> links = linkRepo.findLinkByAnimeId(anime.getId());
        List<Categoria> categories = categoriaAnimeRepo.findCategoryByAnimeId(anime.getId());

        dto.setId(anime.getId());
        dto.setTitle(anime.getTitle());
        dto.setResume(anime.getResume());
        dto.setCultLevel(calcularCultLevel(anime.getId()));
        dto.setState(anime.getState().getName());
        dto.setLinks(links);
        dto.setCategories(categories);

        return dto;
    }

    public List<AnimeDTO> obtenerAnimes() {
        List<Anime> animes = animeRepo.findAll();
        List<AnimeDTO> finalAnimeList = new ArrayList<>();

        for(Anime a : animes) {
            AnimeDTO dto = animeToDTO(a);
            finalAnimeList.add(dto);
        }

        return finalAnimeList;
    }

    public List<AnimeDTO> obtenerAnimesPorCategoria(Integer categoryId) {
        if(categoryId == null) return new ArrayList<>();
        List<Anime> animes = categoriaAnimeRepo.findAnimeByCategoryId(categoryId);
        List<AnimeDTO> finalAnimeList = new ArrayList<>();

        for(Anime a : animes) {
            AnimeDTO dto = animeToDTO(a);
            finalAnimeList.add(dto);
        }

        return finalAnimeList;
    }

    public List<AnimeDTO> obtenerAnimesPorEstado(String state) {
        Optional<State> s = stateRepo.findByName(state);
        if(s.isEmpty()) return new ArrayList<>();

        List<Anime> animes = animeRepo.findByState_Id(s.get().getId());
        List<AnimeDTO> finalAnimeList = new ArrayList<>();

        for(Anime a : animes) {
            AnimeDTO dto = animeToDTO(a);
            finalAnimeList.add(dto);
        }

        return finalAnimeList;
    }

    public AnimeDTO obtenerAnimesPorId(Integer animeId) {
        if(animeId == null) return new AnimeDTO();
        Optional<Anime> a = animeRepo.findById(animeId);
        if(a.isEmpty()) return null;

        AnimeDTO dto = animeToDTO(a.get());
        return dto;
    }

    public ResponseEntity<String> aprobarAnime(String passwd, Integer animeId) {
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(!"admin1234".equals(passwd)) return ResponseEntity.status(401).body("Acceso denegado a las funciones de administrador");

        Optional<Anime> anime = animeRepo.findById(animeId);
        if(anime.isEmpty()) return ResponseEntity.status(404).body("El anime que se intenta aprobar no existe");

        Optional<State> state = stateRepo.findByName("aprobado");
        Anime a = anime.get();
        a.setState(state.get());

        animeRepo.save(a);
        return ResponseEntity.ok("El anime fue aprobado correctamente");
    }

    public ResponseEntity<String> rechazarAnime(String passwd, Integer animeId) {
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(!"admin1234".equals(passwd)) return ResponseEntity.status(401).body("Acceso denegado a las funciones de administrador");

        Optional<Anime> anime = animeRepo.findById(animeId);
        if(anime.isEmpty()) return ResponseEntity.status(404).body("El anime que se intenta rechazar no existe");

        Optional<State> state = stateRepo.findByName("rechazado");
        Anime a = anime.get();
        a.setState(state.get());

        animeRepo.save(a);
        return ResponseEntity.ok("El anime fue rechazado correctamente");
    }

    public ResponseEntity<String> resetearAnime(String passwd, Integer animeId) {
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(!"admin1234".equals(passwd)) return ResponseEntity.status(401).body("Acceso denegado a las funciones de administrador");

        Optional<Anime> anime = animeRepo.findById(animeId);
        if(anime.isEmpty()) return ResponseEntity.status(404).body("El anime al que se le intenta resetear el estado no existe");

        Optional<State> state = stateRepo.findByName("en revision");
        Anime a = anime.get();
        a.setState(state.get());

        animeRepo.save(a);
        return ResponseEntity.ok("Estado del anime reseteado correctamente");
    }
}
