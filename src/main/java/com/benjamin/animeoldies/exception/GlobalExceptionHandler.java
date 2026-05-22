package com.benjamin.animeoldies.exception;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Manejo de excepciones generales
    @ExceptionHandler
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(500).body(ex.getLocalizedMessage());
    }

    // Manejo de errores por Optional.get() donde no exista el elemento
    @ExceptionHandler
    public ResponseEntity<String> handleElementException(NoSuchElementException ex) {
        return ResponseEntity.status(500).body(ex.getLocalizedMessage());
    }

    // Manejo de execptiones por el uso de nulos
    @ExceptionHandler
    public ResponseEntity<String> handleNullException(NullPointerException ex) {
        return ResponseEntity.status(500).body(ex.getLocalizedMessage());
    }
}
