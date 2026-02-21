package com.example.personnel_service.service;

import com.example.personnel_service.entity.MedicalCondition;
import com.example.personnel_service.repository.MedicalConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing MedicalCondition entities.
 * Provides business logic for MedicalCondition CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class MedicalConditionService {
    private final MedicalConditionRepository repository;

    /**
     * Constructs a new MedicalConditionService with the specified repository.
     * 
     * @param repository the MedicalConditionRepository used for data access
     */
    public MedicalConditionService(MedicalConditionRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all medical conditions from the database.
     * 
     * @return list of all MedicalCondition entities
     */
    public List<MedicalCondition> getAllMedicalConditions() {
        return repository.findAll();
    }

    /**
     * Retrieves a medical condition by its unique identifier.
     * 
     * @param id the unique identifier of the medical condition
     * @return the MedicalCondition entity with the specified ID
     * @throws RuntimeException if medical condition with the given ID is not found
     */
    public MedicalCondition getMedicalConditionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical condition not found"));
    }

    /**
     * Adds one or more new medical condition records to the database.
     * 
     * @param medicalConditions list of MedicalCondition entities to be saved
     * @return list of saved MedicalCondition entities with generated IDs
     */
    public List<MedicalCondition> addMedicalCondition(List<MedicalCondition> medicalConditions) {
        return repository.saveAll(medicalConditions);
    }

    /**
     * Updates one or more existing medical condition records in the database.
     * 
     * @param medicalConditions list of MedicalCondition entities with updated information
     * @return list of updated MedicalCondition entities
     */
    public List<MedicalCondition> updateMedicalCondition(List<MedicalCondition> medicalConditions) {
        return repository.saveAll(medicalConditions);
    }

    /**
     * Performs a soft delete by marking a medical condition as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the medical condition to soft delete
     * @return the updated MedicalCondition entity with isDisabled set to true
     * @throws RuntimeException if medical condition with the given ID is not found
     */
    public MedicalCondition softDeleteMedicalCondition(Long id) {
        MedicalCondition medicalCondition = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical condition not found"));
        medicalCondition.setDisabled(true);
        return repository.save(medicalCondition);
    }

    /**
     * Permanently deletes a medical condition record from the database.
     * 
     * @param id the unique identifier of the medical condition to delete
     * @throws RuntimeException if medical condition with the given ID is not found
     */
    public void deleteMedicalCondition(Long id) {
        MedicalCondition medicalCondition = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical condition not found"));
        repository.delete(medicalCondition);
    }
}
