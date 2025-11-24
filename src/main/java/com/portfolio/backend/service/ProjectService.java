package com.portfolio.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.backend.dto.ProjectRequest;
import com.portfolio.backend.entity.Project;
import com.portfolio.backend.repository.ProjectRepository;

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
    public Optional<Project> findProjectById(UUID id) {
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
    public Project createProject(ProjectRequest request) {
        Project project = new Project();
        applyRequest(project, request);
        return projectRepository.save(project);
    }

    /**
     * Deletes a project by its ID.
     *
     * @param id the ID of the project to delete.
     */
    @Transactional
    public void deleteProjectById(UUID id) {
        projectRepository.deleteById(id);
    }

    /**
     * Updates an existing project identified by its ID with the provided data.
     * <p>
     * This method retrieves the project from the database, updates its fields
     * with the values from {@code updatedProject}, and persists the changes.
     * </p>
     *
     * @param projectId      the ID of the project to update
     * @param updatedProject the project object containing updated data
     * @return the updated Project entity
     * @throws IllegalArgumentException if no project is found with the given ID
     */
    @Transactional
    public Project updateProject(UUID projectId, ProjectRequest request) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
        applyRequest(existingProject, request);
        existingProject.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(existingProject);
    }

    private void applyRequest(Project project, ProjectRequest request) {
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setSummary(request.getSummary());
        project.setStatus(request.getStatus());
        project.setCoverImage(request.getCoverImage());
        project.setImages(request.getImages());
        project.setRepoUrl(request.getRepoUrl());
        project.setLiveUrl(request.getLiveUrl());
        project.setTags(request.getTags());
        project.setContent(request.getContent());
        project.setStack(request.getStack());
        project.setFeatures(request.getFeatures());
        project.setContributions(request.getContributions());
        project.setOutcomes(request.getOutcomes());
        project.setClient(request.getClient());
        project.setTestimonial(request.getTestimonial());
        project.setTestimonialAuthor(request.getTestimonialAuthor());
        project.setMood(request.getMood());
        project.setPersonalNote(request.getPersonalNote());
        project.setDuration(request.getDuration());
        project.setRole(request.getRole());
    }

}
