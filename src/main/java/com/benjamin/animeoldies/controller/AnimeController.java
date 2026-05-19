package com.benjamin.animeoldies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.benjamin.animeoldies.model.User;
import com.benjamin.animeoldies.repository.UserRepo;

@RestController
@RequestMapping("/test")
public class AnimeController {
    @Autowired
    private UserRepo repo;

    @GetMapping
    public List<User> get() {
        return repo.findAll();
    }
}