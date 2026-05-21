package com.benjamin.animeoldies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {
    List<Review> findByState_Id(Integer state_id);
    List<Review> findByUser_Id(Integer user_id);
    List<Review> findByAnime_Id(Integer anime_id);
    void deleteByAnime_Id(Integer anime_id);
}