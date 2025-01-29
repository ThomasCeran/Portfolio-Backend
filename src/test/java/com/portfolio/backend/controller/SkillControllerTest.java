package com.portfolio.backend.controller;

import com.portfolio.backend.entity.Skill;
import com.portfolio.backend.service.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the SkillController class.
 */
class SkillControllerTest {

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillController skillController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSkills() {
        List<Skill> skills = Arrays.asList(new Skill(), new Skill());
        when(skillService.findAllSkills()).thenReturn(skills);

        ResponseEntity<List<Skill>> response = skillController.getAllSkills();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(skillService, times(1)).findAllSkills();
    }

    @Test
    void testGetSkillByName() {
        Skill skill = new Skill();
        skill.setName("Java");
        when(skillService.findSkillByName("Java")).thenReturn(Optional.of(skill));

        ResponseEntity<Skill> response = skillController.getSkillByName("Java");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Java", response.getBody().getName());
        verify(skillService, times(1)).findSkillByName("Java");
    }

    @Test
    void testSaveSkill() {
        Skill skill = new Skill();
        skill.setName("Java");
        when(skillService.saveSkill(skill)).thenReturn(skill);

        ResponseEntity<Skill> response = skillController.saveSkill(skill);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Java", response.getBody().getName());
        verify(skillService, times(1)).saveSkill(skill);
    }

    @Test
    void testDeleteSkillById() {
        Long skillId = 1L;

        ResponseEntity<Void> response = skillController.deleteSkillById(skillId);

        assertEquals(204, response.getStatusCode().value());
        verify(skillService, times(1)).deleteSkillById(skillId);
    }

    @Test
    void testGetSkillsByProjectId() {
        List<Skill> skills = Arrays.asList(new Skill(), new Skill());
        when(skillService.findSkillsByProjectId(1L)).thenReturn(skills);

        ResponseEntity<List<Skill>> response = skillController.getSkillsByProjectId(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(skillService, times(1)).findSkillsByProjectId(1L);
    }

    @Test
    void testGetSkillsCreatedAfter() {
        LocalDateTime date = LocalDateTime.now().minusDays(10);
        List<Skill> skills = Arrays.asList(new Skill(), new Skill());
        when(skillService.findSkillsCreatedAfter(date)).thenReturn(skills);

        ResponseEntity<List<Skill>> response = skillController.getSkillsCreatedAfter(date.toString());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(skillService, times(1)).findSkillsCreatedAfter(date);
    }
}
