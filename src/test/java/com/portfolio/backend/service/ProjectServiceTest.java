package com.portfolio.backend.service;

import com.portfolio.backend.dto.ProjectRequest;
import com.portfolio.backend.entity.Project;
import com.portfolio.backend.repository.ProjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProjectService class.
 */
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllProjects() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.findAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void testFindProjectById() {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setId(projectId);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.findProjectById(projectId);

        assertTrue(result.isPresent());
        assertEquals(projectId, result.get().getId());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void testFindProjectsByStatus() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findByStatus("Completed")).thenReturn(projects);

        List<Project> result = projectService.findProjectsByStatus("Completed");

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findByStatus("Completed");
    }

    @Test
    void testFindProjectsCreatedAfter() {
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findByCreatedAtAfter(date)).thenReturn(projects);

        List<Project> result = projectService.findProjectsCreatedAfter(date);

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findByCreatedAtAfter(date);
    }

    @Test
    void testFindProjectsByTitle() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findByTitleContainingIgnoreCase("Java")).thenReturn(projects);

        List<Project> result = projectService.findProjectsByTitle("Java");

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findByTitleContainingIgnoreCase("Java");
    }

    @Test
    void testFindProjectsBySkillName() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findBySkillName("Spring Boot")).thenReturn(projects);

        List<Project> result = projectService.findProjectsBySkillName("Spring Boot");

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findBySkillName("Spring Boot");
    }

    @Test
    void testFindAllProjectsOrderedByCreatedAt() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findAllByOrderByCreatedAtDesc()).thenReturn(projects);

        List<Project> result = projectService.findAllProjectsOrderedByCreatedAt();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void testCreateProject() {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("New Project");
        request.setDescription("Desc");
        request.setStatus("completed");
        Project saved = new Project();
        saved.setTitle("New Project");
        saved.setDescription("Desc");
        saved.setStatus("completed");
        when(projectRepository.save(any(Project.class))).thenReturn(saved);

        Project result = projectService.createProject(request);

        assertNotNull(result);
        assertEquals("New Project", result.getTitle());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testDeleteProjectById() {
        UUID projectId = UUID.randomUUID();

        projectService.deleteProjectById(projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }

    /**
     * Test the updateProject method to ensure it correctly updates and saves a
     * project.
     */
    @Test
    void testUpdateProject_Success() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setTitle("Old Title");
        existingProject.setDescription("Old description");
        existingProject.setStatus("Draft");

        ProjectRequest request = new ProjectRequest();
        request.setTitle("New Title");
        request.setDescription("New description");
        request.setStatus("Completed");
        request.setSummary("Summary");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project result = projectService.updateProject(projectId, request);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New description", result.getDescription());
        assertEquals("Completed", result.getStatus());
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).save(existingProject);
    }

    /**
     * Test the updateProject method when the project does not exist (should throw
     * exception).
     */
    @Test
    void testUpdateProject_ProjectNotFound() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        ProjectRequest request = new ProjectRequest();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.updateProject(projectId, request);
        });

        assertTrue(exception.getMessage().contains("Project not found"));
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }

}
