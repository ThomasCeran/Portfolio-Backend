package com.portfolio.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portfolio.backend.entity.Skill;

/**
 * Repository interface for managing Skill entity operations.
 * This interface extends JpaRepository, providing CRUD functionality and
 * additional custom queries.
 */
public interface SkillRepository extends JpaRepository<Skill, Long> {

    /**
     * Finds a skill by its name.
     *
     * @param name the name of the skill.
     * @return an Optional containing the skill if found, or empty otherwise.
     */
    Optional<Skill> findByName(String name);

    /**
     * Checks if a skill exists with the given name.
     *
     * @param name the name of the skill to check.
     * @return true if a skill exists with the given name, false otherwise.
     */
    boolean existsByName(String name);

    /**
     * Finds all skills associated with a specific project by the project's ID.
     *
     * @param projectId the ID of the project.
     * @return a list of skills associated with the given project.
     */
    @Query("SELECT s FROM Skill s JOIN s.projects p WHERE p.id = :projectId")
    List<Skill> findByProjectId(@Param("projectId") UUID projectId);

    /**
     * Finds all skills created after a specific date.
     *
     * @param createdAt the cutoff creation date.
     * @return a list of skills created after the given date.
     */
    @Query("SELECT s FROM Skill s WHERE s.createdAt > :createdAt")
    List<Skill> findByCreatedAtAfter(@Param("createdAt") LocalDateTime createdAt);
}
