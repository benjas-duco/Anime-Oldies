package com.benjamin.animeoldies.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUuid(UUID uuid);
}