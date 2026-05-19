<<<<<<< HEAD
package com.benjamin.animeoldies.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benjamin.animeoldies.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);
}
=======
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
>>>>>>> a96fb1a (Finished repos and anime service)
