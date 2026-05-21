package com.benjamin.animeoldies.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benjamin.animeoldies.model.Categoria;
import com.benjamin.animeoldies.repository.CategoriaRepo;

@Service
public class CategoryService {
    @Autowired
    CategoriaRepo categoriaRepo;

    public List<Categoria> obtenerCategorias() {
        return categoriaRepo.findAll();
    }

    public String borrarCategoria(Integer categoryId) {
        if(categoryId == null) return "Se debe proporcionar una ID valida";
        Optional<Categoria> cat = categoriaRepo.findById(categoryId);
        if(cat.isEmpty()) return "No existe una categoria con ese ID";
        categoriaRepo.deleteById(categoryId);
        return "Categoria eliminada correctamente";
    }
}
