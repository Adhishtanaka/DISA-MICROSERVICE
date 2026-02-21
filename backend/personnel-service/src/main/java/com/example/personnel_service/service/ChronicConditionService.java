package com.example.personnel_service.service;

import com.example.personnel_service.entity.ChronicCondition;
import com.example.personnel_service.repository.ChronicConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing ChronicCondition entities.
 * Provides business logic for ChronicCondition CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class ChronicConditionService {
    private final ChronicConditionRepository repository;

    /**
     * Constructs a new ChronicConditionService with the specified repository.
     * 
     * @param repository the ChronicConditionRepository used for data access
     */
    public ChronicConditionService(ChronicConditionRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all chronic conditions from the database.
     * 
     * @return list of all ChronicCondition entities
     */
    public List<ChronicCondition> getAllChronicConditions() {
        return repository.findAll();
    }

    /**
     * Retrieves a chronic condition by its unique identifier.
     * 
     * @param id the unique identifier of the chronic condition
     * @return the ChronicCondition entity with the specified ID
     * @throws RuntimeException if chronic condition with the given ID is not found
     */
    public ChronicCondition getChronicConditionById(long id) {
        return repository.findById(id).orElseThrow(()-> new RuntimeException("Chronic condition not found"));
    }

    /**
     * Adds one or more new chronic condition records to the database.
     * 
     * @param chronicCondition list of ChronicCondition entities to be saved
     * @return list of saved ChronicCondition entities with generated IDs
     */
    public List<ChronicCondition> addChronicCondition(List<ChronicCondition> chronicCondition) {
        return repository.saveAll(chronicCondition);
    }

    /**
     * Updates one or more existing chronic condition records in the database.
     * 
     * @param chronicCondition list of ChronicCondition entities with updated information
     * @return list of updated ChronicCondition entities
     */
    public List<ChronicCondition> updateChronicCondition(List<ChronicCondition> chronicCondition) {
        return repository.saveAll(chronicCondition);
    }

    /**
     * Performs a soft delete by marking a chronic condition as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the chronic condition to soft delete
     * @return the updated ChronicCondition entity with isDisabled set to true
     * @throws RuntimeException if chronic condition with the given ID is not found
     */
    public ChronicCondition softDeleteChronicCondition(Long id) {
        ChronicCondition chronicCondition = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Chronic condition not found"));
        chronicCondition.setDisabled(true);
        return repository.save(chronicCondition);
    }
    public void deleteChronicCondition(long id) {
        ChronicCondition chronicCondition = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Chronic condition not found"));
        repository.delete(chronicCondition);
    }

}
