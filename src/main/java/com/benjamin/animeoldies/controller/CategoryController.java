package com.benjamin.animeoldies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.benjamin.animeoldies.model.Categoria;
import com.benjamin.animeoldies.service.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public List<Categoria> obtenerCategorias() {
        return categoryService.obtenerCategorias();
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> borrarCategoria(@PathVariable Integer categoryId) {
        return categoryService.borrarCategoria(categoryId);
    }
}
