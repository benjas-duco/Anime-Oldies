package com.benjamin.animeoldies.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benjamin.animeoldies.DTOs.CategoriaDTO;
import com.benjamin.animeoldies.model.Categoria;
import com.benjamin.animeoldies.repository.CategoriaRepo;

@Service
public class CategoryService {
    @Autowired
    CategoriaRepo categoriaRepo;

    private List<CategoriaDTO> categoriesToDTO(List<Categoria> cats) {
        List<CategoriaDTO> list = new ArrayList<>();
        for(Categoria c : cats) {
            list.add(new CategoriaDTO(c.getId(),c.getName()));
        }
        return list;
    }

    public List<CategoriaDTO> obtenerCategorias() {
        return categoriesToDTO(categoriaRepo.findAll());
    }
}
