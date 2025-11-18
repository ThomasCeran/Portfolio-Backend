package com.portfolio.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.portfolio.backend.dto.ProjectRequest;
import com.portfolio.backend.entity.Project;
import com.portfolio.backend.service.ProjectService;

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
    void testGetProjectById_found() {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setId(projectId);
        when(projectService.findProjectById(projectId)).thenReturn(Optional.of(project));

        ResponseEntity<Project> response = projectController.getProjectById(projectId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(projectId, response.getBody().getId());
    }

    @Test
    void testGetProjectById_notFound() {
        UUID projectId = UUID.randomUUID();
        when(projectService.findProjectById(projectId)).thenReturn(Optional.empty());

        ResponseEntity<Project> response = projectController.getProjectById(projectId);

        assertEquals(404, response.getStatusCode().value());
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

    @Test
    void testCreateProject() {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("New");
        request.setDescription("Desc");
        request.setStatus("completed");
        Project created = new Project();
        when(projectService.createProject(any(ProjectRequest.class))).thenReturn(created);

        ResponseEntity<Project> response = projectController.createProject(request);

        assertEquals(201, response.getStatusCode().value());
        verify(projectService, times(1)).createProject(request);
    }

    @Test
    void testUpdateProject() {
        UUID projectId = UUID.randomUUID();
        ProjectRequest request = new ProjectRequest();
        request.setTitle("Updated");
        request.setDescription("Desc");
        request.setStatus("completed");
        Project updated = new Project();
        when(projectService.updateProject(projectId, request)).thenReturn(updated);

        ResponseEntity<Project> response = projectController.updateProject(projectId, request);

        assertEquals(200, response.getStatusCode().value());
        verify(projectService, times(1)).updateProject(projectId, request);
    }

    @Test
    void testDeleteProject() {
        UUID projectId = UUID.randomUUID();

        ResponseEntity<Void> response = projectController.deleteProject(projectId);

        assertEquals(204, response.getStatusCode().value());
        verify(projectService, times(1)).deleteProjectById(projectId);
    }
}
