package com.portfolio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "AuthRequest", description = "Identifiants utilisés pour obtenir un JWT")
public class AuthRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email administrateur", example = "thomas.ceran.dev@gmail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Mot de passe administrateur", example = "********")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
