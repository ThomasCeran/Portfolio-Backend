package com.portfolio.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

/**
 * Représente un projet réalisé par un utilisateur.
 */
@Entity
public class Project {

    // Identifiant unique pour chaque projet (généré automatiquement par la base de
    // données)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Titre du projet
    private String title;

    // Description du projet
    private String description;

    // URL du projet (par exemple, un lien vers un site ou un dépôt GitHub)
    private String url;

    // URL de l'image associée au projet (peut être utilisé pour l'affichage)
    private String imageUrl;

    // Statut du projet (par exemple, "En cours", "Terminé")
    private String status;

    // Date et heure de création du projet (non modifiable une fois créé)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Date et heure de la dernière mise à jour du projet
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Liste des compétences liées au projet (relation Many-to-Many)
    @ManyToMany
    @JoinTable(name = "project_skill", // Table de jointure entre "project" et "skill"
            joinColumns = @JoinColumn(name = "project_id"), // Colonne qui relie le projet
            inverseJoinColumns = @JoinColumn(name = "skill_id") // Colonne qui relie les compétences
    )
    private List<Skill> skills;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

}