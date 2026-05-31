package com.benjamin.animeoldies.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.Categoria;

@Repository
public interface CategoriaRepo extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByName(String name);
    Optional<Categoria> findByNameIgnoreCase(String name);
}