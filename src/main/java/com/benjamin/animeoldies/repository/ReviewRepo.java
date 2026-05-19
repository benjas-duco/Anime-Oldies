package com.benjamin.animeoldies.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {
    List<Review> findByState_Id(Integer stateId);
    List<Review> findByUser_Id(UUID userId);
    List<Review> findByAnime_Id(Integer animeId);
}