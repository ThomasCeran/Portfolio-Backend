package com.portfolio.backend.controller;

import com.portfolio.backend.entity.Project;
import com.portfolio.backend.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProjectController class.
 */
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProjects() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectService.findAllProjects()).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getAllProjects();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(projectService, times(1)).findAllProjects();
    }

    @Test
    void testGetProjectsByStatus() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectService.findProjectsByStatus("Completed")).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getProjectsByStatus("Completed");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(projectService, times(1)).findProjectsByStatus("Completed");
    }

    @Test
    void testGetProjectsCreatedAfter() {
        LocalDateTime date = LocalDateTime.now().minusDays(10);
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectService.findProjectsCreatedAfter(date)).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getProjectsCreatedAfter(date);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(projectService, times(1)).findProjectsCreatedAfter(date);
    }

    @Test
    void testGetProjectsByTitle() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectService.findProjectsByTitle("Java")).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getProjectsByTitle("Java");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(projectService, times(1)).findProjectsByTitle("Java");
    }

    @Test
    void testGetProjectsBySkill() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectService.findProjectsBySkillName("Spring Boot")).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getProjectsBySkill("Spring Boot");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(projectService, times(1)).findProjectsBySkillName("Spring Boot");
    }

    // @Test
    // void testSaveProject() {
    //     Project project = new Project();
    //     project.setTitle("New Project");
    //     when(projectService.saveProject(project)).thenReturn(project);

    //     ResponseEntity<Project> response = projectController.saveProject(project);

    //     assertEquals(200, response.getStatusCode().value());
    //     assertNotNull(response.getBody());
    //     verify(projectService, times(1)).saveProject(project);
    // }

    // @Test
    // void testDeleteProjectById() {
    //     Long projectId = 1L;

    //     ResponseEntity<Void> response = projectController.deleteProjectById(projectId);

    //     assertEquals(204, response.getStatusCode().value());
    //     verify(projectService, times(1)).deleteProjectById(projectId);
    // }
}
