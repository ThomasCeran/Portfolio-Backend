package com.portfolio.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.backend.dto.ProjectRequest;
import com.portfolio.backend.entity.Project;
import com.portfolio.backend.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing Project entity operations.
 */
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Retrieves all projects.
     *
     * @return a list of all projects.
     */
    @Operation(summary = "Liste des projets publiés")
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.findAllProjects());
    }

    /**
     * Retrieves project details by id.
     *
     * @param projectId the identifier of the project
     * @return the project or 404 if not found
     */
    @Operation(summary = "Détails d'un projet")
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable UUID projectId) {
        return projectService.findProjectById(projectId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves projects by status.
     *
     * @param status the status of the projects.
     * @return a list of projects with the given status.
     */
    @Operation(summary = "Recherche par statut")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Project>> getProjectsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(projectService.findProjectsByStatus(status));
    }

    /**
     * Retrieves projects created after a specific date.
     *
     * @param createdAt the cutoff creation date.
     * @return a list of projects created after the given date.
     */
    @Operation(summary = "Projets créés après une date ISO (yyyy-MM-ddTHH:mm:ss)")
    @GetMapping("/createdAfter/{createdAt}")
    public ResponseEntity<List<Project>> getProjectsCreatedAfter(@PathVariable LocalDateTime createdAt) {
        return ResponseEntity.ok(projectService.findProjectsCreatedAfter(createdAt));
    }

    /**
     * Retrieves projects by title.
     *
     * @param title the title of the project.
     * @return a list of projects containing the given title.
     */
    @Operation(summary = "Recherche par titre (contains, case insensitive)")
    @GetMapping("/search")
    public ResponseEntity<List<Project>> getProjectsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(projectService.findProjectsByTitle(title));
    }

    /**
     * Retrieves projects associated with a specific skill name.
     *
     * @param skillName the name of the skill.
     * @return a list of projects associated with the given skill.
     */
    @Operation(summary = "Projets liés à une skill")
    @GetMapping("/skill/{skillName}")
    public ResponseEntity<List<Project>> getProjectsBySkill(@PathVariable String skillName) {
        return ResponseEntity.ok(projectService.findProjectsBySkillName(skillName));
    }

    /**
     * Creates a new project (admin only).
     */
    @Operation(summary = "Créer un projet", security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Créé"),
                    @ApiResponse(responseCode = "401", description = "Non authentifié")
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectRequest request) {
        Project created = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Updates an existing project (admin only).
     */
    @Operation(summary = "Mettre à jour un projet", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> updateProject(@PathVariable UUID projectId,
            @Valid @RequestBody ProjectRequest request) {
        Project updated = projectService.updateProject(projectId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a project (admin only).
     */
    @Operation(summary = "Supprimer un projet", security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(responseCode = "204", description = "Supprimé"))
    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }

}
