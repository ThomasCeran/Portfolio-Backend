package com.portfolio.backend.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Payload used to create/update projects from the admin UI.
 */
@Schema(name = "ProjectInput", description = "Payload utilisé pour créer ou mettre à jour un projet")
public class ProjectRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "Titre du projet", example = "Autonoma")
    private String title;

    @NotBlank(message = "Description is required")
    @Schema(description = "Description longue", example = "Application SaaS full-stack (Spring Boot + React).")
    private String description;

    @Schema(description = "Résumé court pour les cards", example = "Refonte API + UI pour accélérer l'onboarding.")
    private String summary;

    @NotBlank(message = "Status is required")
    @Schema(description = "Statut du projet", example = "completed")
    private String status;

    @Schema(description = "Image principale affichée sur la card", example = "https://cdn.dev/projets/autonoma/cover.png")
    private String coverImage;

    @Schema(description = "Galerie d'images supplémentaires")
    private List<String> images;

    @Schema(description = "URL du repository Git", example = "https://github.com/thomasc/autonoma")
    private String repoUrl;

    @Schema(description = "URL live du projet", example = "https://autonoma.app")
    private String liveUrl;

    @Schema(description = "Tags affichés sur la card", example = "[\"spring-boot\",\"react\",\"design-system\"]")
    private List<String> tags;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
