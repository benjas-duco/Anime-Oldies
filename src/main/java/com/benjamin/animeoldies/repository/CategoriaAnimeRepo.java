<<<<<<< HEAD
package com.benjamin.animeoldies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.Anime;
import com.benjamin.animeoldies.model.CategoriaAnime;

@Repository
public interface CategoriaAnimeRepo extends JpaRepository<CategoriaAnime, Long> {
    @Query("SELECT ca.anime FROM CategoriaAnime ca WHERE ca.category.id = :category_id")
    List<Anime> findAnimeByCategoryId(@Param("category_id") Long categoryId);
}
=======
package com.benjamin.animeoldies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.Anime;
import com.benjamin.animeoldies.model.Categoria;
import com.benjamin.animeoldies.model.CategoriaAnime;

@Repository
public interface CategoriaAnimeRepo extends JpaRepository<CategoriaAnime, Integer> {
    @Query("SELECT ca.anime FROM CategoriaAnime ca WHERE ca.category.id = :category_id")
    List<Anime> findAnimeByCategoryId(@Param("category_id") Integer categoryId);

    @Query("SELECT ca.category FROM CategoriaAnime ca WHERE ca.anime.id = :anime_id")
    List<Categoria> findCategoryByAnimeId(@Param("anime_id") Integer animeId);
}
>>>>>>> a96fb1a (Finished repos and anime service)
