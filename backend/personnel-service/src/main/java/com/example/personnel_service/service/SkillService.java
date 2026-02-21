package com.example.personnel_service.service;

import com.example.personnel_service.entity.Skill;
import com.example.personnel_service.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Skill entities.
 * Provides business logic for Skill CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class SkillService {
    private final SkillRepository repository;

    /**
     * Constructs a new SkillService with the specified repository.
     * 
     * @param repository the SkillRepository used for data access
     */
    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all skills from the database.
     * 
     * @return list of all Skill entities
     */
    public List<Skill> getAllSkills() {
        return repository.findAll();
    }

    /**
     * Retrieves a skill by its unique identifier.
     * 
     * @param id the unique identifier of the skill
     * @return the Skill entity with the specified ID
     * @throws RuntimeException if skill with the given ID is not found
     */
    public Skill getSkillById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    /**
     * Adds one or more new skill records to the database.
     * 
     * @param skills list of Skill entities to be saved
     * @return list of saved Skill entities with generated IDs
     */
    public List<Skill> addSkill(List<Skill> skills) {
        return repository.saveAll(skills);
    }

    /**
     * Updates one or more existing skill records in the database.
     * 
     * @param skills list of Skill entities with updated information
     * @return list of updated Skill entities
     */
    public List<Skill> updateSkill(List<Skill> skills) {
        return repository.saveAll(skills);
    }

    /**
     * Performs a soft delete by marking a skill as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the skill to soft delete
     * @return the updated Skill entity with isDisabled set to true
     * @throws RuntimeException if skill with the given ID is not found
     */
    public Skill softDeleteSkill(Long id) {
        Skill skill = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        skill.setDisabled(true);
        return repository.save(skill);
    }

    /**
     * Permanently deletes a skill record from the database.
     * 
     * @param id the unique identifier of the skill to delete
     * @throws RuntimeException if skill with the given ID is not found
     */
    public void deleteSkill(Long id) {
        Skill skill = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        repository.delete(skill);
    }
}
