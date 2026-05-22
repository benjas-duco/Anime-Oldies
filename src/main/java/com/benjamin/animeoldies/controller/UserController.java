package com.benjamin.animeoldies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.benjamin.animeoldies.DTOs.UserDTO;
import com.benjamin.animeoldies.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.obtenerUsuarios();
    }

    @GetMapping("/users/{userId}")
    public UserDTO getUserByID(@PathVariable Integer userId) {
        return userService.obtenerUsuarioPorId(userId);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@RequestParam String passwd, @PathVariable Integer userId) {
        return userService.borrarUsuario(passwd, userId);
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody UserDTO user) {
        return userService.agregarUsuario(user);
    }

    @PutMapping("/users/{userId}/{username}")
    public ResponseEntity<String> renameUser(@PathVariable Integer userId, @PathVariable String username) {
        return userService.renombrarUsuario(userId, username);
    }
}
