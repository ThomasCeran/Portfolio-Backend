package com.portfolio.backend.service;

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
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.findProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(projectRepository, times(1)).findById(1L);
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
    void testSaveProject() {
        Project project = new Project();
        project.setTitle("New Project");
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.saveProject(project);

        assertNotNull(result);
        assertEquals("New Project", result.getTitle());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testDeleteProjectById() {
        Long projectId = 1L;

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
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setTitle("Old Title");
        existingProject.setDescription("Old description");
        existingProject.setStatus("Draft");

        Project updatedProject = new Project();
        updatedProject.setTitle("New Title");
        updatedProject.setDescription("New description");
        updatedProject.setStatus("Completed");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project result = projectService.updateProject(projectId, updatedProject);

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
        Long projectId = 999L;
        Project updatedProject = new Project();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.updateProject(projectId, updatedProject);
        });

        assertTrue(exception.getMessage().contains("Project not found"));
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }

}
