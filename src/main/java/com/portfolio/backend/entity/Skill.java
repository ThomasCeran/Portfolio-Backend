package com.portfolio.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;

/**
 * Classe représentant une compétence dans l'application.
 * Chaque compétence peut être associée à plusieurs projets.
 */
@Entity // Indique que cette classe est une entité JPA et sera mappée à une table dans
        // la base de données.
public class Skill {

    /**
     * Identifiant unique pour chaque compétence.
     * Il est généré automatiquement par la base de données grâce à la stratégie
     * GenerationType.IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom de la compétence (par exemple : "Java", "Spring", "React").
     */
    private String name;

    /**
     * Niveau de maîtrise de la compétence (par exemple : "Débutant",
     * "Intermédiaire", "Avancé").
     */
    private String level;

    /**
     * URL ou chemin de l'icône représentant la compétence.
     * Cette icône peut être affichée dans l'interface utilisateur.
     */
    private String icon;

    /**
     * Date de création de l'entité.
     * Ce champ est obligatoire et ne peut pas être modifié après sa création.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date de dernière mise à jour de l'entité.
     * Ce champ est mis à jour lorsqu'une modification est effectuée.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Liste des projets associés à cette compétence.
     * Relation Many-to-Many : Une compétence peut être utilisée dans plusieurs
     * projets,
     * et un projet peut avoir plusieurs compétences.
     */
    @ManyToMany(mappedBy = "skills") // Définit que cette entité est le côté inverse de la relation avec "skills"
                                     // dans Project.
    private List<Project> projects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

}
