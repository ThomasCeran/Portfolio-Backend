package com.portfolio.backend.service;

import com.portfolio.backend.entity.Project;
import com.portfolio.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Project entity operations.
 * Provides business logic and interacts with the ProjectRepository.
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * Constructor-based dependency injection.
     *
     * @param projectRepository The repository for managing Project entities.
     */
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Retrieves all projects.
     *
     * @return a list of all projects.
     */
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Finds a project by its ID.
     *
     * @param id the ID of the project.
     * @return an Optional containing the project if found, or empty otherwise.
     */
    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * Finds projects by their status.
     *
     * @param status the status of the project (e.g., "In Progress", "Completed").
     * @return a list of projects with the given status.
     */
    public List<Project> findProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    /**
     * Finds projects created after a specific date.
     *
     * @param createdAt the cutoff creation date.
     * @return a list of projects created after the given date.
     */
    public List<Project> findProjectsCreatedAfter(LocalDateTime createdAt) {
        return projectRepository.findByCreatedAtAfter(createdAt);
    }

    /**
     * Finds projects by a keyword in their title.
     *
     * @param title the title keyword to search for.
     * @return a list of projects containing the keyword in their title.
     */
    public List<Project> findProjectsByTitle(String title) {
        return projectRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Finds projects that are associated with a specific skill name.
     *
     * @param skillName the name of the skill.
     * @return a list of projects associated with the given skill.
     */
    public List<Project> findProjectsBySkillName(String skillName) {
        return projectRepository.findBySkillName(skillName);
    }

    /**
     * Retrieves all projects ordered by creation date in descending order.
     *
     * @return a list of projects sorted by the most recent first.
     */
    public List<Project> findAllProjectsOrderedByCreatedAt() {
        return projectRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Saves a new or updates an existing project.
     *
     * @param project the project entity to save.
     * @return the saved project.
     */
    @Transactional
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    /**
     * Deletes a project by its ID.
     *
     * @param id the ID of the project to delete.
     */
    @Transactional
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }
}
