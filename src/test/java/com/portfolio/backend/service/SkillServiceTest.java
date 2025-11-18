package com.portfolio.backend.service;

import com.portfolio.backend.entity.Skill;
import com.portfolio.backend.repository.SkillRepository;
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
import static org.mockito.Mockito.*;

/**
 * Unit tests for SkillService class.
 */
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllSkills() {
        List<Skill> skills = Arrays.asList(new Skill(), new Skill());
        when(skillRepository.findAll()).thenReturn(skills);

        List<Skill> result = skillService.findAllSkills();

        assertEquals(2, result.size());
        verify(skillRepository, times(1)).findAll();
    }

    @Test
    void testFindSkillByName() {
        Skill skill = new Skill();
        skill.setName("Java");
        when(skillRepository.findByName("Java")).thenReturn(Optional.of(skill));

        Optional<Skill> result = skillService.findSkillByName("Java");

        assertTrue(result.isPresent());
        assertEquals("Java", result.get().getName());
        verify(skillRepository, times(1)).findByName("Java");
    }

    @Test
    void testExistsByName() {
        when(skillRepository.existsByName("Java")).thenReturn(true);

        boolean exists = skillService.existsByName("Java");

        assertTrue(exists);
        verify(skillRepository, times(1)).existsByName("Java");
    }

    @Test
    void testSaveSkill() {
        Skill skill = new Skill();
        skill.setName("Java");
        when(skillRepository.save(skill)).thenReturn(skill);

        Skill result = skillService.saveSkill(skill);

        assertNotNull(result);
        assertEquals("Java", result.getName());
        verify(skillRepository, times(1)).save(skill);
    }

    @Test
    void testDeleteSkillById() {
        Long skillId = 1L;

        skillService.deleteSkillById(skillId);

        verify(skillRepository, times(1)).deleteById(skillId);
    }

    @Test
    void testFindSkillsByProjectId() {
        List<Skill> skills = Arrays.asList(new Skill(), new Skill());
        UUID projectId = UUID.randomUUID();
        when(skillRepository.findByProjectId(projectId)).thenReturn(skills);

        List<Skill> result = skillService.findSkillsByProjectId(projectId);

        assertEquals(2, result.size());
        verify(skillRepository, times(1)).findByProjectId(projectId);
    }

    @Test
    void testFindSkillsCreatedAfter() {
        LocalDateTime date = LocalDateTime.now().minusDays(10);
        List<Skill> skills = Arrays.asList(new Skill(), new Skill());
        when(skillRepository.findByCreatedAtAfter(date)).thenReturn(skills);

        List<Skill> result = skillService.findSkillsCreatedAfter(date);

        assertEquals(2, result.size());
        verify(skillRepository, times(1)).findByCreatedAtAfter(date);
    }
}
