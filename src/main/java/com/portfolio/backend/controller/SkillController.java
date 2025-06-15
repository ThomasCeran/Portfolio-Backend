package com.portfolio.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.backend.entity.Skill;
import com.portfolio.backend.service.SkillService;

/**
 * Controller for managing Skill entity operations.
 */
@RestController
@RequestMapping("/api/admin/skills")
@PreAuthorize("hasRole('ADMIN')")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * Fetch all skills.
     * 
     * @return List of all skills.
     */
    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> skills = skillService.findAllSkills();
        return ResponseEntity.ok(skills);
    }

    /**
     * Fetch a skill by its name.
     * 
     * @param name Name of the skill.
     * @return The skill if found.
     */
    @GetMapping("/{name}")
    public ResponseEntity<Skill> getSkillByName(@PathVariable String name) {
        Optional<Skill> skill = skillService.findSkillByName(name);
        return skill.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new skill.
     * 
     * @param skill Skill object to save.
     * @return The saved skill.
     */
    @PostMapping
    public ResponseEntity<Skill> saveSkill(@RequestBody Skill skill) {
        Skill savedSkill = skillService.saveSkill(skill);
        return ResponseEntity.ok(savedSkill);
    }

    /**
     * Delete a skill by its ID.
     * 
     * @param id ID of the skill to delete.
     * @return HTTP 204 if deleted successfully.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillById(@PathVariable Long id) {
        skillService.deleteSkillById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetch skills by project ID.
     * 
     * @param projectId Project ID to filter skills.
     * @return List of skills associated with the project.
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Skill>> getSkillsByProjectId(@PathVariable Long projectId) {
        List<Skill> skills = skillService.findSkillsByProjectId(projectId);
        return ResponseEntity.ok(skills);
    }

    /**
     * Fetch skills created after a specific date.
     * 
     * @param createdAt Date to filter skills.
     * @return List of skills created after the specified date.
     */
    @GetMapping("/created-after")
    public ResponseEntity<List<Skill>> getSkillsCreatedAfter(@RequestParam String createdAt) {
        LocalDateTime date = LocalDateTime.parse(createdAt);
        List<Skill> skills = skillService.findSkillsCreatedAfter(date);
        return ResponseEntity.ok(skills);
    }
}
