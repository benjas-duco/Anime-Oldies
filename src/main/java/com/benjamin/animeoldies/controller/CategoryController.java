package com.benjamin.animeoldies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.benjamin.animeoldies.DTOs.CategoriaDTO;
import com.benjamin.animeoldies.service.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoriaDTO> obtenerCategorias() {
        return categoryService.obtenerCategorias();
    }
}
