package com.portfolio.backend.controller;

import com.portfolio.backend.entity.Project;
import com.portfolio.backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for admin operations on projects (add, update, delete).
 * All endpoints are protected and require ADMIN role.
 */
@RestController
@RequestMapping("/api/admin/projects")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProjectController {

    private final ProjectService projectService;

    public AdminProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Adds a new project.
     * 
     * @param project The project to add.
     * @return The saved project.
     */
    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        Project saved = projectService.saveProject(project);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates an existing project by ID.
     * 
     * @param projectId      The ID of the project to update.
     * @param updatedProject The project data to update.
     * @return The updated project.
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long projectId,
            @RequestBody Project updatedProject) {
        Project saved = projectService.updateProject(projectId, updatedProject);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a project by ID.
     * 
     * @param projectId The project ID.
     * @return No content if successful.
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }

    /**
     * (Optionnel) Récupère tous les projets pour l’admin (tu peux sécuriser ou pas)
     */
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.findAllProjects());
    }
}
