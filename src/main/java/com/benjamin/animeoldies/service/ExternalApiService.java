package com.benjamin.animeoldies.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {
    RestTemplate call = new RestTemplate();

    public String obtenerCotizacionPesoChileno() {
        String response =  call.getForObject(
            "https://cl.dolarapi.com/v1/cotizaciones",
            String.class
        );
        System.out.println(response);

        return response;
    }
}
