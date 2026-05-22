package com.benjamin.animeoldies.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.benjamin.animeoldies.DTOs.UserDTO;
import com.benjamin.animeoldies.model.User;
import com.benjamin.animeoldies.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    private UserDTO userToDTO(User user) {
        UserDTO dto = new UserDTO();
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

    public ResponseEntity<String> borrarUsuario(String passwd, Integer userId) {
        if(userId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(!"admin1234".equals(passwd)) return ResponseEntity.status(401).body("Acceso denegado a las funciones de administrador");
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()) return ResponseEntity.status(404).body("El usuario que se intenta eliminar no existe");
        userRepo.deleteById(userId);
        return ResponseEntity.ok("El usuario se elimino correctamente");
    }

    public ResponseEntity<String> agregarUsuario(UserDTO user) {
        if(user.getNickname() == null || user.getNickname().strip().equals("")) 
            return ResponseEntity.badRequest().body("No se puede proporcionar un nombre de usuario nulo o vacio");
        User newUser = new User();
        newUser.setNickname(user.getNickname());
        userRepo.save(newUser);
        return ResponseEntity.ok("Usuario agregado con exito");
    }

    public ResponseEntity<String> renombrarUsuario(Integer userId, String nombre) {
        if(userId == null) return ResponseEntity.badRequest().body("Se debe proporcionar una ID valida");
        if(nombre == null || nombre.strip().equals("")) return ResponseEntity.badRequest().body("No se puede proporcionar un nombre de usuario nulo o vacio");
        Optional<User> usuario = userRepo.findById(userId);
        if(usuario.isEmpty()) return ResponseEntity.status(404).body("El usuario que se intenta modificar no existe");
        User user = usuario.get();
        user.setNickname(nombre);
        userRepo.save(user);
        return ResponseEntity.ok("Nombre de usuario modificado correctamente");
    }
}
