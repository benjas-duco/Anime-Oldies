package com.benjamin.animeoldies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.Link;

@Repository
public interface LinkRepo extends JpaRepository<Link, Integer> {
    @Query("SELECT l FROM Link l WHERE l.anime.id = :anime_id")
    List<Link> findLinkByAnimeId(@Param("anime_id") Integer animeId);

    void deleteByAnime_Id(Integer anime_id);
}
