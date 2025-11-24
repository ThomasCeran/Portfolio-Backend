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

    @Schema(description = "Contenu long / case study", example = "Un article détaillant les choix d'architecture...")
    private String content;

    @Schema(description = "Stack principale", example = "[\"Spring Boot\",\"React\",\"PostgreSQL\"]")
    private List<String> stack;

    @Schema(description = "Fonctionnalités clés", example = "[\"Auth JWT\",\"CI/CD\",\"Dark mode\"]")
    private List<String> features;

    @Schema(description = "Contributions principales", example = "[\"Refonte API\",\"Migration vers React 18\"]")
    private List<String> contributions;

    @Schema(description = "Résultats/impacts", example = "[\"Temps de chargement -40%\",\"Taux de conversion +12%\"]")
    private List<String> outcomes;

    @Schema(description = "Client ou entreprise", example = "ACME Corp")
    private String client;

    @Schema(description = "Témoignage ou feedback du client", example = "\"Excellent delivery et super com\"")
    private String testimonial;

    @Schema(description = "Auteur du témoignage", example = "Jane Doe, CTO")
    private String testimonialAuthor;

    @Schema(description = "Humeur / ton / mood", example = "Bold & minimal")
    private String mood;

    @Schema(description = "Note personnelle / story", example = "Ce projet m'a appris à...")
    private String personalNote;

    @Schema(description = "Durée ou timeline", example = "Janv - Mars 2024")
    private String duration;

    @Schema(description = "Rôle joué", example = "Backend lead")
    private String role;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getStack() {
        return stack;
    }

    public void setStack(List<String> stack) {
        this.stack = stack;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<String> getContributions() {
        return contributions;
    }

    public void setContributions(List<String> contributions) {
        this.contributions = contributions;
    }

    public List<String> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<String> outcomes) {
        this.outcomes = outcomes;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTestimonial() {
        return testimonial;
    }

    public void setTestimonial(String testimonial) {
        this.testimonial = testimonial;
    }

    public String getTestimonialAuthor() {
        return testimonialAuthor;
    }

    public void setTestimonialAuthor(String testimonialAuthor) {
        this.testimonialAuthor = testimonialAuthor;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getPersonalNote() {
        return personalNote;
    }

    public void setPersonalNote(String personalNote) {
        this.personalNote = personalNote;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
