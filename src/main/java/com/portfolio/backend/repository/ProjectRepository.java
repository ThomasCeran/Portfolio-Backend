package com.portfolio.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portfolio.backend.entity.Project;

/**
 * Repository interface for managing Project entity operations.
 * This interface extends JpaRepository, providing CRUD functionality and
 * additional custom queries.
 */
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    /**
     * Finds projects by their status.
     *
     * @param status the status of the project (e.g., "In Progress", "Completed").
     * @return a list of projects with the given status.
     */
    List<Project> findByStatus(String status);

    /**
     * Finds projects created after a specific date.
     *
     * @param createdAt the cutoff creation date.
     * @return a list of projects created after the given date.
     */
    List<Project> findByCreatedAtAfter(LocalDateTime createdAt);

    /**
     * Finds projects by their title.
     *
     * @param title the title of the project.
     * @return a list of projects with the given title.
     */
    List<Project> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds projects that are associated with a specific skill name.
     *
     * @param skillName the name of the skill.
     * @return a list of projects associated with the given skill.
     */
    @Query("SELECT p FROM Project p JOIN p.skills s WHERE LOWER(s.name) = LOWER(:skillName)")
    List<Project> findBySkillName(@Param("skillName") String skillName);

    /**
     * Finds all projects and orders them by their creation date in descending
     * order.
     *
     * @return a list of projects ordered by creation date (most recent first).
     */
    List<Project> findAllByOrderByCreatedAtDesc();
}
