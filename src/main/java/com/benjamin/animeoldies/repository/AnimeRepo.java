package com.benjamin.animeoldies.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.Anime;

@Repository
public interface AnimeRepo extends JpaRepository<Anime, Integer> {
    Optional<Anime> findByTitle(String title);
    Optional<Anime> findByTitleIgnoreCase(String title);
    List<Anime> findByTitleContainingIgnoreCase(String title);
    List<Anime> findByState_Id(Integer stateId);
}
