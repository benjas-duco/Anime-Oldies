package com.benjamin.animeoldies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.benjamin.animeoldies.service.ExternalApiService;

@RestController
@RequestMapping("/api/v1")
public class ExternalApiController {
    @Autowired
    ExternalApiService externalApiService;

    @GetMapping("/external")
    public String obtenerCotizacionPesoChileno() {
        return externalApiService.obtenerCotizacionPesoChileno();
    }
}
