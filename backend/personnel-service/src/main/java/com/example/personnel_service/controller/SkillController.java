package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Skill;
import com.example.personnel_service.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Skill entities.
 * Provides endpoints for CRUD operations on personnel skill records.
 * Base path: /api/personnel/skills
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/skills")
public class SkillController {
    private final SkillService skillService;

    /**
     * Constructs a new SkillController with the specified SkillService.
     * 
     * @param skillService the service used to handle Skill business logic
     */
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * Retrieves all skills from the database.
     * 
     * @return ResponseEntity containing a list of all Skill entities
     */
    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    /**
     * Retrieves a specific skill by its unique identifier.
     * 
     * @param id the unique identifier of the skill to retrieve
     * @return ResponseEntity containing the Skill entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    /**
     * Creates one or more new skill records.
     * 
     * @param skills list of Skill entities to create
     * @return ResponseEntity containing the created Skill entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<Skill>> createSkills(@RequestBody List<Skill> skills) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.addSkill(skills));
    }

    /**
     * Updates one or more existing skill records.
     * 
     * @param skills list of Skill entities with updated information
     * @return ResponseEntity containing the updated Skill entities
     */
    @PutMapping
    public ResponseEntity<List<Skill>> updateSkills(@RequestBody List<Skill> skills) {
        return ResponseEntity.ok(skillService.updateSkill(skills));
    }

    /**
     * Performs a soft delete on a skill by marking it as disabled.
     * The skill record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the skill to soft delete
     * @return ResponseEntity containing the soft-deleted Skill entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Skill> softDeleteSkill(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.softDeleteSkill(id));
    }

    /**
     * Permanently deletes a skill record from the database.
     * 
     * @param id the unique identifier of the skill to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
