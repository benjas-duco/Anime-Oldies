package com.benjamin.animeoldies.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.benjamin.animeoldies.DTOs.AnimeDTO;
import com.benjamin.animeoldies.DTOs.AnimeAddDTO;
import com.benjamin.animeoldies.DTOs.CategoriaDTO;
import com.benjamin.animeoldies.DTOs.ContentBody;
import com.benjamin.animeoldies.DTOs.LinkDTO;
import com.benjamin.animeoldies.DTOs.ReviewDTO;
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

import jakarta.transaction.Transactional;

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

    public ResponseEntity<String> agregarAnimeCategoria(Integer animeId, String catName){
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Anime> anOG = animeRepo.findById(animeId);
        if(anOG.isEmpty()) return ResponseEntity.status(404).body("El anime no existe");

        if (catName == null || catName.strip().equals("")) return ResponseEntity.badRequest().body("El nombre de la categoria no puede estar vacio o nulo");
        Optional<Categoria> cat = categoriaRepo.findByNameIgnoreCase(catName);

        if(cat.isEmpty()) {
            Categoria category = new Categoria();
            category.setName(catName);
            Categoria c = categoriaRepo.save(category);

            CategoriaAnime ca = new CategoriaAnime();
            ca.setAnime(anOG.get());
            ca.setCategory(c);
            categoriaAnimeRepo.save(ca);

            return ResponseEntity.ok("La categoria fue creada correctamente (ID: "+c.getId()+")");
        }else{
            CategoriaAnime ca = new CategoriaAnime();
            ca.setAnime(anOG.get());
            ca.setCategory(cat.get());
            categoriaAnimeRepo.save(ca);

            return ResponseEntity.ok("La categoria ya existia (ID: "+cat.get().getId()+") y se enlazo con el anime correctamente");
        }
    }

    @Transactional
    public ResponseEntity<String> eliminarAnimeCategoria(Integer cateId) {
        if(cateId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Categoria> cat = categoriaRepo.findById(cateId);
        if(cat.isEmpty()) return ResponseEntity.status(404).body("La categoria con ese id no existe");

        categoriaAnimeRepo.deleteByCategory_id(cateId);
        categoriaRepo.deleteById(cateId);

        return ResponseEntity.ok("La categoria fue borrada correctamente");
    }

    public ResponseEntity<String> editarAnimeCategoria(CategoriaDTO cate) {
        if(cate.getId() == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Categoria> cat = categoriaRepo.findById(cate.getId());
        if(cat.isEmpty()) return ResponseEntity.status(404).body("La categoria con ese id no existe");

        if (cate.getName() == null || cate.getName().strip().equals("")) return ResponseEntity.badRequest().body("El nombre de la categoria no puede estar vacio o nulo");
        Optional<Categoria> catTest = categoriaRepo.findByNameIgnoreCase(cate.getName());
        if(!catTest.isEmpty()) return ResponseEntity.status(409).body("Ya existe una categoria con ese nombre");

        Categoria category = cat.get();
        category.setName(cate.getName());
        categoriaRepo.save(category);

        return ResponseEntity.ok("La categoria fue editada correctamente");
    }

    public ResponseEntity<String> eliminarAnimeLink(Integer linkId) {
        if (linkId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Link> link = linkRepo.findById(linkId);
        if (link.isEmpty()) return ResponseEntity.status(404).body("Ese link no existe");

        linkRepo.deleteById(linkId);
        return ResponseEntity.ok("El link fue borrado correctamente");
    }

    public ResponseEntity<String> editarAnimeLink(LinkDTO newlnk) {
        if (newlnk.getId() == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Link> linkGet = linkRepo.findById(newlnk.getId());
        if (linkGet.isEmpty()) return ResponseEntity.status(404).body("El link que se intenta editar no existe");
        Link link = linkGet.get();

        if(newlnk.getName() != null) {
            if(newlnk.getName().strip().equals("")) return ResponseEntity.badRequest().body("El nombre del link no puede estar vacio");
            link.setName(newlnk.getName());
        }
        if (newlnk.getLink() != null) {
            if(newlnk.getLink().strip().equals("")) return ResponseEntity.badRequest().body("El link no puede estar vacio");
            link.setLink(newlnk.getLink());
        }

        linkRepo.save(link);
        return ResponseEntity.ok("El link fue editado correctamente");
    }

    public ResponseEntity<String> agregarAnimeLink(Integer animeId, LinkDTO link) {
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Anime> anOG = animeRepo.findById(animeId);
        if(anOG.isEmpty()) return ResponseEntity.status(404).body("Anime no encontrado");

        if(link == null) return ResponseEntity.badRequest().body("Se debe proporcionar datos del link");
        if(link.getName() == null || link.getName().strip().equals("")) return ResponseEntity.badRequest().body("El nombre del link esta vacio o es nulo");
        if(link.getLink() == null || link.getLink().strip().equals("")) return ResponseEntity.badRequest().body("El link esta vacio o es nulo");

        Link lnk = new Link();
        
        lnk.setAnime(anOG.get());
        lnk.setName(link.getName());
        lnk.setLink(link.getLink());

        Link fnLink = linkRepo.save(lnk);

        return ResponseEntity.ok("Link agregado correctamente (ID: "+fnLink.getId()+")");
    }

    public ResponseEntity<String> actualizarAnimeResumen(Integer animeId, ContentBody content) {
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Anime> anOG = animeRepo.findById(animeId);
        if(anOG.isEmpty()) return ResponseEntity.status(404).body("Anime no encontrado");
        if(content.getContent() == null || content.getContent().strip().equals("")) return ResponseEntity.badRequest().body("Se debe proporcionar un texto valido");
        Anime anime = anOG.get();
        anime.setResume(content.getContent());
        animeRepo.save(anime);
        return ResponseEntity.ok("Anime actualizado correctamente");
    }

    public ResponseEntity<String> actualizarAnimeNombre(Integer animeId, String nombre) {
        if(animeId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Anime> anOG = animeRepo.findById(animeId);
        if(anOG.isEmpty()) return ResponseEntity.status(404).body("Anime no encontrado");
        if(nombre == null || nombre.strip().equals("")) return ResponseEntity.badRequest().body("Se debe proporcionar un nombre valido");
        Optional<Anime> an = animeRepo.findByTitleIgnoreCase(nombre);
        if(!an.isEmpty() && an.get().getId() != animeId) return ResponseEntity.status(409).body("Ya existe un anime con ese nombre");
        Anime anime = anOG.get();
        anime.setTitle(nombre);
        animeRepo.save(anime);
        return ResponseEntity.ok("Anime actualizado correctamente");
    }

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

    @Transactional
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

    @Transactional
    private void borrarCategorias(Integer animeID) {
        categoriaAnimeRepo.deleteByAnime_Id(animeID);
    }

    @Transactional
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

    public ResponseEntity<String> agregarAnime(AnimeAddDTO anime) {
        if (anime.getTitle() == null || anime.getTitle().strip().equals("")) 
            return ResponseEntity.badRequest().body("El titulo del anime no puede ser nulo o estar en blanco");
        if (anime.getResume() == null || anime.getResume().strip().equals("")) 
            return ResponseEntity.badRequest().body("El resumen del anime no puede ser nulo o estar en blanco");
        if (anime.getCategories() == null || anime.getCategories().isEmpty()) 
            return ResponseEntity.badRequest().body("Este anime no puede no tener ninguna categoria asociada");

        Optional<Anime> an = animeRepo.findByTitleIgnoreCase(anime.getTitle());
        if(!an.isEmpty()) return ResponseEntity.status(409).body("Ya existe un Anime con este titulo");

        Anime a = new Anime();

        if (anime.getLinks() != null) {
            int rel = testLinks(anime.getLinks());
            if(rel == 1) return ResponseEntity.badRequest().body("No se puede crear el anime con un link vacio o nulo");
            if(rel == 2) return ResponseEntity.badRequest().body("No se puede crear el anime con un link sin nombre o nulo");
        }

        int rel = testCategories(anime.getCategories());

        if(rel == 1) return ResponseEntity.badRequest().body("No se puede actualizar el anime con una categoria vacia o nula");

        a.setTitle(anime.getTitle());
        a.setResume(anime.getResume());
        a.setState(stateRepo.findByName("en revision").get());

        Anime saved = animeRepo.save(a);

        if (anime.getLinks() != null) agregarLinks(saved.getId(),anime.getLinks());
        agregarCategorias(saved.getId(),anime.getCategories());

        return ResponseEntity.ok("Anime agregado correctamente (ID: "+saved.getId()+")");
    }

    public State obtenerEstado(Integer animeId) {
        if(animeId == null) return new State();
        Optional<Anime> anime = animeRepo.findById(animeId);
        if(anime.isEmpty()) return new State();

        State estado = anime.get().getState();
        return estado;
    }

    private ReviewDTO reviewToDTO(Review rev) {
        ReviewDTO review = new ReviewDTO();
        review.setId(rev.getId());
        review.setAnimeId(rev.getAnime().getId());
        review.setUserId(rev.getUser().getId());
        review.setBody(rev.getBody());
        review.setScore(rev.getScore());
        review.setState(rev.getState().getName());
        return review;
    }

    public List<ReviewDTO> obtenerReviews(Integer animeId, String state) {
        if(animeId == null) return new ArrayList<>();
        List<Review> reviews = reviewRepo.findByAnime_Id(animeId);
        List<ReviewDTO> final_list = new ArrayList<>();

        for(Review rev : reviews) {
            if(rev.getState().getName().equals(state)) {
                final_list.add(reviewToDTO(rev));
            }
        }

        return final_list;
    }

    private double calcularCultLevel(Integer animeId) {
        List<ReviewDTO> reviews = obtenerReviews(animeId, "aprobado");
        double reviewCount = reviews.size();
        double scoreSum = 0;

        for(ReviewDTO rev : reviews) {
            scoreSum += rev.getScore();
        }

        if(reviewCount == 0) return 0;

        double averageScore = scoreSum / reviewCount;

        return averageScore / Math.log(reviewCount + 1);
    }

    public List<CategoriaDTO> obtenerCategorias(Integer animeId) {
        if(animeId == null) return new ArrayList<>();
        List<Categoria> cats = categoriaAnimeRepo.findCategoryByAnimeId(animeId);
        return categoriesToDTO(cats);
    }

    public List<LinkDTO> obtenerLinks(Integer animeId) {
        if(animeId == null) return new ArrayList<>();
        List<Link> links = linkRepo.findLinkByAnimeId(animeId);
        return linksToDTO(links);
    }

    private List<LinkDTO> linksToDTO(List<Link> links) {
        List<LinkDTO> list = new ArrayList<>();
        for(Link l : links) {
            list.add(new LinkDTO(l.getId(),l.getLink(),l.getName()));
        }
        return list;
    }

    private List<CategoriaDTO> categoriesToDTO(List<Categoria> cats) {
        List<CategoriaDTO> list = new ArrayList<>();
        for(Categoria c : cats) {
            list.add(new CategoriaDTO(c.getId(),c.getName()));
        }
        return list;
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
        dto.setLinks(linksToDTO(links));
        dto.setCategories(categoriesToDTO(categories));

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
