package com.benjamin.animeoldies.DTOs;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
