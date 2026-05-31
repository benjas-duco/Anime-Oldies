package com.benjamin.animeoldies.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.benjamin.animeoldies.DTOs.ReviewDTO;
import com.benjamin.animeoldies.DTOs.ReviewUpdateDTO;
import com.benjamin.animeoldies.model.Anime;
import com.benjamin.animeoldies.model.Review;
import com.benjamin.animeoldies.model.State;
import com.benjamin.animeoldies.model.User;
import com.benjamin.animeoldies.repository.AnimeRepo;
import com.benjamin.animeoldies.repository.ReviewRepo;
import com.benjamin.animeoldies.repository.StateRepo;
import com.benjamin.animeoldies.repository.UserRepo;

@Service
public class ReviewService {
    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    StateRepo stateRepo;

    @Autowired
    AnimeRepo animeRepo;

    public List<ReviewDTO> obtenerTodasLasReviews() {
        List<Review> reviews = reviewRepo.findAll();
        List<ReviewDTO> finList = new ArrayList<>();

        for(Review r : reviews) {
            finList.add(reviewToDTO(r));
        }

        return finList;
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

    public List<ReviewDTO> obtenerReviewsPorEstado(String state) {
        Optional<State> estado = stateRepo.findByName(state);
        if(estado.isEmpty()) return new ArrayList<>();
        List<Review> reviews = reviewRepo.findByState_Id(estado.get().getId());
        List<ReviewDTO> finList = new ArrayList<>();

        for(Review r : reviews) {
            finList.add(reviewToDTO(r));
        }

        return finList;
    }

    public List<ReviewDTO> obtenerReviewsPorUsuario(Integer userId) {
        if(userId == null) return new ArrayList<>();

        List<Review> reviews = reviewRepo.findByUser_Id(userId);
        List<ReviewDTO> finList = new ArrayList<>();

        for(Review r : reviews) {
            finList.add(reviewToDTO(r));
        }

        return finList;
    }

    public ResponseEntity<String> agregarReview(ReviewDTO review) {
        if(review.getAnimeId() == null) return ResponseEntity.badRequest().body("No se proporciona una ID valida para anime");
        if(review.getUserId() == null) return ResponseEntity.badRequest().body("No se proporciona una ID valida para usario");

        Optional<User> user = userRepo.findById(review.getUserId());
        Optional<Anime> anime = animeRepo.findById(review.getAnimeId());

        if(user.isEmpty()) return ResponseEntity.status(404).body("El usuario con el que se intenta publicar la review no existe");
        if(anime.isEmpty()) return ResponseEntity.status(404).body("El anime en el cual se intenta publicar la review no existe");
        if(review.getBody() == null || review.getBody().strip().equals("")) 
            return ResponseEntity.badRequest().body("La review no puede tener un contenido vacio");
        if(review.getScore() == null || review.getScore() < 0 || review.getScore() > 10) 
            return ResponseEntity.badRequest().body("El puntaje de la review debe estar de entre 0 y 10");

        Review postReview = new Review();

        postReview.setAnime(anime.get());
        postReview.setUser(user.get());
        postReview.setBody(review.getBody());
        postReview.setScore(review.getScore());
        postReview.setState(stateRepo.findByName("en revision").get());

        Review rev = reviewRepo.save(postReview);
        return ResponseEntity.ok("Review agregada correctamente (ID: "+rev.getId()+")");
    }

    public ResponseEntity<String> eliminarReview(Integer reviewId) {
        if(reviewId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(reviewRepo.findById(reviewId).isEmpty()) return ResponseEntity.status(404).body("La review que se intenta eliminar no existe");
        reviewRepo.deleteById(reviewId);
        return ResponseEntity.ok("Review eliminada correctamente");
    }

    public ResponseEntity<String> actualizarReview(ReviewUpdateDTO newReview) {
        if(newReview.getId() == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        Optional<Review> review = reviewRepo.findById(newReview.getId());
        if(review.isEmpty()) return ResponseEntity.status(404).body("La review que se intenta actualizar no existe");

        Review rev = review.get();

        if(newReview.getBody() != null) {
            if (newReview.getBody().strip().equals("")) return ResponseEntity.badRequest().body("El contenido de la review no puede estar vacio");
            rev.setBody(newReview.getBody());
        }

        if(newReview.getScore() != null) {
            if(newReview.getScore() < 0 || newReview.getScore() > 10) return ResponseEntity.badRequest().body("El puntaje de la review debe estar de entre 0 y 10");
            rev.setScore(newReview.getScore());
        }

        reviewRepo.save(rev);
        return ResponseEntity.ok("Review actualizada correctamente");
    }

    public ResponseEntity<String> aprobarReview(String passwd, Integer reviewId) {
        if(reviewId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(!"admin1234".equals(passwd)) return ResponseEntity.status(401).body("Acceso denegado a las funciones de administrador");
        Optional<Review> review = reviewRepo.findById(reviewId);

        if(review.isEmpty()) return ResponseEntity.status(404).body("La review que se intenta aprobar no existe");
        Optional<State> state = stateRepo.findByName("aprobado");

        Review r = review.get();
        r.setState(state.get());
        reviewRepo.save(r);

        return ResponseEntity.ok("La review ha sido aprobada correctamente");
    }

    public ResponseEntity<String> rechazarReview(String passwd, Integer reviewId) {
        if(reviewId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(!"admin1234".equals(passwd)) return ResponseEntity.status(401).body("Acceso denegado a las funciones de administrador");
        Optional<Review> review = reviewRepo.findById(reviewId);

        if(review.isEmpty()) return ResponseEntity.status(404).body("La review que se intenta rechazar no existe");
        Optional<State> state = stateRepo.findByName("rechazado");

        Review r = review.get();
        r.setState(state.get());
        reviewRepo.save(r);

        return ResponseEntity.ok("La review ha sido rechazada correctamente");
    }

    public ResponseEntity<String> resetearEstadoDeReview(String passwd, Integer reviewId) {
        if(reviewId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(!"admin1234".equals(passwd)) return ResponseEntity.status(401).body("Acceso denegado a las funciones de administrador");
        Optional<Review> review = reviewRepo.findById(reviewId);

        if(review.isEmpty()) return ResponseEntity.status(404).body("La review a la que se le intenta resetear el estado no existe");
        Optional<State> state = stateRepo.findByName("en revision");

        Review r = review.get();
        r.setState(state.get());
        reviewRepo.save(r);

        return ResponseEntity.ok("El estado de la review ha sido reseteada correctamente");
    }
}
