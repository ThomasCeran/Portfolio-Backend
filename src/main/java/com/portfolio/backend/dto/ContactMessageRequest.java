package com.portfolio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO used to receive contact form data from frontend.
 */
@Schema(name = "MessageInput", description = "Payload du formulaire de contact public")
public class ContactMessageRequest {

    @NotBlank(message = "Name is required")
    @Schema(description = "Nom complet du visiteur", example = "Thomas Ceran")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email de contact", example = "thomas.ceran.dev@gmail.com")
    private String email;

    @NotBlank(message = "Subject is required")
    @Schema(description = "Sujet du message", example = "Collaboration")
    private String subject;

    @NotBlank(message = "Message is required")
    @Schema(description = "Contenu du message", example = "Salut, j'aimerais parler d'un projet freelance.")
    private String message;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
