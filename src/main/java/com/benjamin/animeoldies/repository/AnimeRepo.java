package com.benjamin.animeoldies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.Anime;

@Repository
public interface AnimeRepo extends JpaRepository<Anime, Long> {
    List<Anime> findByTitleContainingIgnoreCase(String title);
    List<Anime> findByState_Id(Long stateId);
}
