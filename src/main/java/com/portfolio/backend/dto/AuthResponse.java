package com.portfolio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthResponse", description = "Réponse contenant le JWT d'authentification")
public class AuthResponse {

    @Schema(description = "Jeton JWT signé à utiliser dans l'en-tête Authorization", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
