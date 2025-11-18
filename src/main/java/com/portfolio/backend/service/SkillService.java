package com.portfolio.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.backend.entity.Skill;
import com.portfolio.backend.repository.SkillRepository;

/**
 * Service class for managing Skill operations.
 */
@Service
@Transactional
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    /**
     * Retrieves all skills.
     *
     * @return a list of all skills.
     */
    @Transactional(readOnly = true)
    public List<Skill> findAllSkills() {
        return skillRepository.findAll();
    }

    /**
     * Finds a skill by its name.
     *
     * @param name the name of the skill.
     * @return an Optional containing the skill if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<Skill> findSkillByName(String name) {
        return skillRepository.findByName(name);
    }

    /**
     * Checks if a skill exists by its name.
     *
     * @param name the name of the skill to check.
     * @return true if the skill exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return skillRepository.existsByName(name);
    }

    /**
     * Saves a new or existing skill.
     *
     * @param skill the skill to save.
     * @return the saved skill.
     */
    public Skill saveSkill(Skill skill) {
        if (skill.getCreatedAt() == null) {
            skill.setCreatedAt(LocalDateTime.now());
        }
        skill.setUpdatedAt(LocalDateTime.now());
        return skillRepository.save(skill);
    }

    /**
     * Deletes a skill by its ID.
     *
     * @param id the ID of the skill to delete.
     */
    public void deleteSkillById(Long id) {
        skillRepository.deleteById(id);
    }

    /**
     * Finds skills associated with a specific project ID.
     *
     * @param projectId the ID of the project.
     * @return a list of skills associated with the project.
     */
    @Transactional(readOnly = true)
    public List<Skill> findSkillsByProjectId(UUID projectId) {
        return skillRepository.findByProjectId(projectId);
    }

    /**
     * Finds skills created after a specific date.
     *
     * @param date the cutoff date.
     * @return a list of skills created after the given date.
     */
    @Transactional(readOnly = true)
    public List<Skill> findSkillsCreatedAfter(LocalDateTime date) {
        return skillRepository.findByCreatedAtAfter(date);
    }
}
