package com.portfolio.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.portfolio.backend.entity.Project;
import com.portfolio.backend.entity.Skill;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Utilise la vraie base de données si configurée
@ActiveProfiles("test") // Utilise le profil de test avec H2
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        // Ajouter des données de test avant chaque test
        Project project1 = new Project();
        project1.setTitle("Portfolio Website");
        project1.setDescription("A personal portfolio website.");
        project1.setStatus("Completed");
        project1.setCreatedAt(LocalDateTime.now().minusDays(10));
        project1.setUpdatedAt(LocalDateTime.now());

        Project project2 = new Project();
        project2.setTitle("E-commerce Platform");
        project2.setDescription("An online e-commerce store.");
        project2.setStatus("In Progress");
        project2.setCreatedAt(LocalDateTime.now().minusDays(5));
        project2.setUpdatedAt(LocalDateTime.now());

        projectRepository.save(project1);
        projectRepository.save(project2);
    }

    @Test
    void testFindByStatus() {
        List<Project> completedProjects = projectRepository.findByStatus("Completed");
        assertEquals(1, completedProjects.size());
        assertEquals("Portfolio Website", completedProjects.get(0).getTitle());
    }

    @Test
    void testFindByCreatedAtAfter() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);
        List<Project> recentProjects = projectRepository.findByCreatedAtAfter(cutoffDate);
        assertEquals(1, recentProjects.size());
        assertEquals("E-commerce Platform", recentProjects.get(0).getTitle());
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        List<Project> projects = projectRepository.findByTitleContainingIgnoreCase("portfolio");
        assertEquals(1, projects.size());
        assertEquals("Portfolio Website", projects.get(0).getTitle());
    }

    @Test
    void testFindBySkillName() {
        // Créer et sauvegarder une entité Skill
        Skill skill = new Skill();
        skill.setName("Java");
        skillRepository.save(skill); // Sauvegarder d'abord le skill dans la base de données

        // Créer un projet et l'associer au skill existant
        Project project = new Project();
        project.setTitle("Java Project");
        project.setDescription("A project related to Java.");
        project.setStatus("Completed");
        project.setCreatedAt(LocalDateTime.now().minusDays(15));
        project.setUpdatedAt(LocalDateTime.now());
        project.setSkills(List.of(skill)); // Associer le skill persisté
        projectRepository.save(project); // Ensuite sauvegarder le projet

        // Vérifier que le projet est récupérable par le nom du skill
        List<Project> javaProjects = projectRepository.findBySkillName("Java");
        assertEquals(1, javaProjects.size());
        assertEquals("Java Project", javaProjects.get(0).getTitle());
    }

    @Test
    void testFindAllByOrderByCreatedAtDesc() {
        List<Project> projects = projectRepository.findAllByOrderByCreatedAtDesc();
        assertEquals(2, projects.size());
        assertEquals("E-commerce Platform", projects.get(0).getTitle()); // Le plus récent
        assertEquals("Portfolio Website", projects.get(1).getTitle());
    }
}