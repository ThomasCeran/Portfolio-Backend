package com.portfolio.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Représente un message de contact envoyé par un utilisateur.
 */
@Entity
public class ContactMessage {
    // Identifiant unique pour chaque message (généré automatiquement par la base de
    // données)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Contenu du message
    private String message;

    // Date et heure de création du message (non modifiable une fois créé)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Référence à l'utilisateur qui a envoyé le message (relation Many-to-One)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Définit la colonne user_id comme clé étrangère
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
