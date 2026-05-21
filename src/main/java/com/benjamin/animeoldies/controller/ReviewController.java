package com.benjamin.animeoldies.controller;

import java.util.List;
import java.util.UUID;

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

import com.benjamin.animeoldies.DTOs.ReviewDTO;
import com.benjamin.animeoldies.DTOs.ReviewUpdateDTO;
import com.benjamin.animeoldies.model.Review;
import com.benjamin.animeoldies.service.ReviewService;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return reviewService.obtenerTodasLasReviews();
    }

    @GetMapping("/reviews/by-state/{state}")
    public List<Review> getReviewsByState(@PathVariable String state) {
        return reviewService.obtenerReviewsPorEstado(state);
    }

    @GetMapping("/reviews/by-user/{userId}")
    public List<Review> getReviewsByUser(@PathVariable Integer userId) {
        return reviewService.obtenerReviewsPorUsuario(userId);
    }

    @PostMapping("/reviews")
    public String addReview(@RequestBody ReviewDTO review) {
        return reviewService.agregarReview(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public String deleteReview(@PathVariable Integer reviewId) {
        return reviewService.eliminarReview(reviewId);
    }

    @PutMapping("/reviews/{reviewId}")
    public String updateReview(@PathVariable Integer reviewId, @RequestBody ReviewUpdateDTO newReview) {
        return reviewService.actualizarReview(reviewId, newReview);
    }

    @PutMapping("/reviews/{reviewId}/aprove")
    public String aproveReview(@RequestParam String passwd, @PathVariable Integer reviewId) {
        return reviewService.aprobarReview(passwd, reviewId);
    }

    @PutMapping("/reviews/{reviewId}/decline")
    public String declineReview(@RequestParam String passwd, @PathVariable Integer reviewId) {
        return reviewService.rechazarReview(passwd, reviewId);
    }

    @PutMapping("/reviews/{reviewId}/reset")
    public String resetReview(@RequestParam String passwd, @PathVariable Integer reviewId) {
        return reviewService.resetearEstadoDeReview(passwd, reviewId);
    }
}
