package com.portfolio.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.portfolio.backend.entity.Project;
import com.portfolio.backend.entity.Skill;

/**
 * Unit tests for SkillRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
class SkillRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @BeforeEach
    void setUp() {
        skillRepository.deleteAll();
    }

    @Test
    void testFindByName() {
        Skill skill = new Skill();
        skill.setName("Java");
        skill.setLevel("Advanced");
        skill.setCreatedAt(LocalDateTime.now().minusDays(10));
        skill.setUpdatedAt(LocalDateTime.now());

        skillRepository.save(skill);

        Optional<Skill> result = skillRepository.findByName("Java");

        assertTrue(result.isPresent());
        assertEquals("Java", result.get().getName());
    }

    @Test
    void testExistsByName() {
        Skill skill = new Skill();
        skill.setName("Spring");
        skill.setLevel("Intermediate");
        skill.setCreatedAt(LocalDateTime.now().minusDays(5));
        skill.setUpdatedAt(LocalDateTime.now());

        skillRepository.save(skill);

        boolean exists = skillRepository.existsByName("Spring");
        assertTrue(exists);
    }

    @Test
    void testFindByProjectId() {
        // Create the Project entity (simulate saving manually)
        Project project = new Project();
        project.setId(1L); // Manually set the ID since ProjectRepository is not available
        project.setTitle("React Project");
        project.setDescription("A project using React.");
        project.setCreatedAt(LocalDateTime.now().minusDays(10));
        project.setUpdatedAt(LocalDateTime.now());

        // Create the Skill entity and associate it with the simulated Project
        Skill skill = new Skill();
        skill.setName("React");
        skill.setLevel("Beginner");
        skill.setCreatedAt(LocalDateTime.now().minusDays(7));
        skill.setUpdatedAt(LocalDateTime.now());
        skill.setProjects(List.of(project));

        // Save the Skill entity
        skillRepository.save(skill);

        // Fetch the skills associated with the project ID
        List<Skill> skills = skillRepository.findByProjectId(1L); // Use the manually set ID
        assertEquals(1, skills.size());
        assertEquals("React", skills.get(0).getName());
    }

    @Test
    void testFindByCreatedAtAfter() {
        Skill oldSkill = new Skill();
        oldSkill.setName("HTML");
        oldSkill.setLevel("Beginner");
        oldSkill.setCreatedAt(LocalDateTime.now().minusDays(20));
        oldSkill.setUpdatedAt(LocalDateTime.now().minusDays(15));

        Skill newSkill = new Skill();
        newSkill.setName("CSS");
        newSkill.setLevel("Intermediate");
        newSkill.setCreatedAt(LocalDateTime.now().minusDays(5));
        newSkill.setUpdatedAt(LocalDateTime.now());

        skillRepository.saveAll(List.of(oldSkill, newSkill));

        List<Skill> recentSkills = skillRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(10));
        assertEquals(1, recentSkills.size());
        assertEquals("CSS", recentSkills.get(0).getName());
    }
}
