package com.portfolio.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.backend.entity.Project;
import com.portfolio.backend.service.ProjectService;

/**
 * REST controller for managing Project entity operations.
 */
@RestController
@RequestMapping("/api/projects")
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

    // /**
    //  * Saves a new project.
    //  *
    //  * @param project the project to be saved.
    //  * @return the saved project.
    //  */
    // @PostMapping
    // public ResponseEntity<Project> saveProject(@RequestBody Project project) {
    //     return ResponseEntity.ok(projectService.saveProject(project));
    // }

    // /**
    //  * Deletes a project by ID.
    //  *
    //  * @param projectId the ID of the project to be deleted
    //  * @return response indicating success or failure.
    //  */
    // @DeleteMapping("/{projectId}")
    // public ResponseEntity<Void> deleteProjectById(@PathVariable Long projectId) {
    //     projectService.deleteProjectById(projectId);
    //     return ResponseEntity.noContent().build();
    // }

    // /**
    //  * Updates an existing project by its ID.
    //  *
    //  * @param projectId      the ID of the project to update
    //  * @param updatedProject the updated project data to apply
    //  * @return the updated project as a response entity
    //  */
    // @PutMapping("/{projectId}")
    // public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody Project updatedProject) {
    //     Project saved = projectService.updateProject(projectId, updatedProject);
    //     return ResponseEntity.ok(saved);
    // }

}
