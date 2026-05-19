package com.benjamin.animeoldies.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.State;

@Repository
public interface StateRepo extends JpaRepository<State, Integer> {
    Optional<State> findById(Integer id);
    Optional<State> findByName(String name);
}
