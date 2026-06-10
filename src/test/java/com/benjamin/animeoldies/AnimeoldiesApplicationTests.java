package com.benjamin.animeoldies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.benjamin.animeoldies.DTOs.AnimeAddDTO;
import com.benjamin.animeoldies.DTOs.AnimeDTO;
import com.benjamin.animeoldies.DTOs.CategoriaDTO;
import com.benjamin.animeoldies.model.State;
import com.benjamin.animeoldies.DTOs.UserDTO;
import com.benjamin.animeoldies.model.Anime;
import com.benjamin.animeoldies.model.User;
import com.benjamin.animeoldies.repository.AnimeRepo;
import com.benjamin.animeoldies.repository.StateRepo;
import com.benjamin.animeoldies.repository.UserRepo;
import com.benjamin.animeoldies.service.AnimeService;
import com.benjamin.animeoldies.service.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AnimeoldiesApplicationTests {

	@Mock
	StateRepo stateRepo;

	@Mock
	UserRepo userRepo;

	@Mock
	AnimeRepo animeRepo;

	@InjectMocks
	AnimeService animeService;

	@InjectMocks
	UserService userService;

	@Test
	void deberiaAgregarUsuario() {
		User userTest = new User();
		userTest.setId(1);

		UserDTO user = new UserDTO();
		user.setNickname("Francisco");

		when(userRepo.findByNickname("Francisco")).thenReturn(Optional.empty());
		when(userRepo.save(any(User.class))).thenReturn(userTest);

		ResponseEntity<String> response = userService.agregarUsuario(user);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void deberiaNoAgregarUsuario() {
		User userTest = new User();
		userTest.setNickname("francisco");

		UserDTO user = new UserDTO();
		user.setNickname("Francisco");

		when(userRepo.findByNickname("Francisco")).thenReturn(Optional.of(userTest));

		ResponseEntity<String> response = userService.agregarUsuario(user);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	void deberiaNoAñadirAnime() {
		Anime animeTest = new Anime();
		animeTest.setTitle("anime");

		List<CategoriaDTO> cats = new ArrayList<>();
		cats.add(new CategoriaDTO());

		AnimeAddDTO anime = new AnimeAddDTO();
		anime.setTitle("anime");
		anime.setResume("resume");
		anime.setCategories(cats);

		when(animeRepo.findByTitleIgnoreCase(anyString())).thenReturn(Optional.of(animeTest));

		ResponseEntity<String> response = animeService.agregarAnime(anime);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	void deberiaAprobarAnime() {
		Anime animeTest = new Anime();
		State state = new State();

		when(animeRepo.findById(1)).thenReturn(Optional.of(animeTest));

		when(stateRepo.findByName(anyString())).thenReturn(Optional.of(state));

		ResponseEntity<String> response = animeService.aprobarAnime("admin1234",1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
