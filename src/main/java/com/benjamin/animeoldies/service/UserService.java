package com.benjamin.animeoldies.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.benjamin.animeoldies.DTOs.UserDTO;
import com.benjamin.animeoldies.model.User;
import com.benjamin.animeoldies.repository.ReviewRepo;
import com.benjamin.animeoldies.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    ReviewRepo reviewRepo;

    private UserDTO userToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNickname(user.getNickname());
        return dto;
    }

    public List<UserDTO> obtenerUsuarios() {
        List<User> users = userRepo.findAll();
        List<UserDTO> usuarios = new ArrayList<>();
        for(User u : users) {
            usuarios.add(userToDTO(u));
        }
        return usuarios;
    }

    public UserDTO obtenerUsuarioPorId(Integer userId) {
        if(userId == null) return new UserDTO();
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()) return new UserDTO();
        return userToDTO(user.get());
    }

    @Transactional
    private void borrarReviews(Integer userId) {
        reviewRepo.deleteByUser_id(userId);
    }

    @Transactional
    public ResponseEntity<String> borrarUsuario(String passwd, Integer userId) {
        if(userId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(!"admin1234".equals(passwd)) return ResponseEntity.status(401).body("Acceso denegado a las funciones de administrador");
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()) return ResponseEntity.status(404).body("El usuario que se intenta eliminar no existe");
        borrarReviews(userId);
        userRepo.deleteById(userId);
        return ResponseEntity.ok("El usuario se elimino correctamente");
    }

    public ResponseEntity<String> agregarUsuario(UserDTO user) {
        if(user.getNickname() == null || user.getNickname().strip().equals("")) 
            return ResponseEntity.badRequest().body("No se puede proporcionar un nombre de usuario nulo o vacio");
        Optional<User> usuario = userRepo.findByNickname(user.getNickname());
        if(!usuario.isEmpty()) return ResponseEntity.status(409).body("Ya existe un usuario con ese nombre, intente otro");
        User newUser = new User();
        newUser.setNickname(user.getNickname());
        User usr = userRepo.save(newUser);
        return ResponseEntity.ok("Usuario agregado con exito (ID: "+usr.getId()+")");
    }

    public ResponseEntity<String> renombrarUsuario(Integer userId, String nombre) {
        if(userId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(nombre == null || nombre.strip().equals("")) return ResponseEntity.badRequest().body("No se puede proporcionar un nombre de usuario nulo o vacio");
        Optional<User> usuario = userRepo.findById(userId);
        if(usuario.isEmpty()) return ResponseEntity.status(404).body("El usuario que se intenta modificar no existe");
        Optional<User> usr = userRepo.findByNickname(nombre);
        if(!usr.isEmpty()) {
            if(usr.get().getId() == usuario.get().getId()) return ResponseEntity.status(409).body("El usuario ya tiene este nombre puesto");
            return ResponseEntity.status(409).body("Ya existe un usuario con ese nombre, intente otro");
        }
        User user = usuario.get();
        user.setNickname(nombre);
        userRepo.save(user);
        return ResponseEntity.ok("Nombre de usuario modificado correctamente");
    }
}
