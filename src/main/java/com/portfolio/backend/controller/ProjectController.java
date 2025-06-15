package com.portfolio.backend.controller;

import com.portfolio.backend.entity.Project;
import com.portfolio.backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing Project entity operations.
 */
@RestController
@RequestMapping("/api/admin/projects")
@PreAuthorize("hasRole('ADMIN')")
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
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.findAllProjects());
    }

    /**
     * Retrieves projects by status.
     *
     * @param status the status of the projects.
     * @return a list of projects with the given status.
     */
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
    @GetMapping("/skill/{skillName}")
    public ResponseEntity<List<Project>> getProjectsBySkill(@PathVariable String skillName) {
        return ResponseEntity.ok(projectService.findProjectsBySkillName(skillName));
    }

    /**
     * Saves a new project.
     *
     * @param project the project to be saved.
     * @return the saved project.
     */
    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.saveProject(project));
    }

    /**
     * Deletes a project by ID.
     *
     * @param projectId the ID of the project to be deleted.
     * @return response indicating success or failure.
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long projectId) {
        projectService.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }
}
