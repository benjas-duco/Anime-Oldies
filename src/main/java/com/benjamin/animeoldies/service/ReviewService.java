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

    public List<Review> obtenerTodasLasReviews() {
        return reviewRepo.findAll();
    }

    public List<Review> obtenerReviewsPorEstado(String state) {
        Optional<State> estado = stateRepo.findByName(state);
        if(estado.isEmpty()) return new ArrayList<>();

        return reviewRepo.findByState_Id(estado.get().getId());
    }

    public List<Review> obtenerReviewsPorUsuario(Integer userId) {
        if(userId == null) return new ArrayList<>();
        return reviewRepo.findByUser_Id(userId);
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

        reviewRepo.save(postReview);
        return ResponseEntity.ok("Review agregada correctamente");
    }

    public ResponseEntity<String> eliminarReview(Integer reviewId) {
        if(reviewId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(reviewRepo.findById(reviewId).isEmpty()) return ResponseEntity.status(404).body("La review que se intenta eliminar no existe");
        reviewRepo.deleteById(reviewId);
        return ResponseEntity.ok("Review eliminada correctamente");
    }

    public ResponseEntity<String> actualizarReview(Integer reviewId, ReviewUpdateDTO newReview) {
        if(reviewId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(newReview.getBody() == null || newReview.getBody().strip().equals("")) 
            return ResponseEntity.badRequest().body("La review no puede tener un contenido vacio");
        if(newReview.getScore() == null || newReview.getScore() < 0 || newReview.getScore() > 10) 
            return ResponseEntity.badRequest().body("El puntaje de la review debe estar de entre 0 y 10");

        Optional<Review> review = reviewRepo.findById(reviewId);

        if(review.isEmpty()) return ResponseEntity.status(404).body("La review que se intenta actualizar no existe");

        review.get().setBody(newReview.getBody());
        review.get().setScore(newReview.getScore());

        reviewRepo.save(review.get());
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
