package com.example.personnel_service.service;

import com.example.personnel_service.entity.Medication;
import com.example.personnel_service.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Medication entities.
 * Provides business logic for Medication CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class MedicationService {
    private final MedicationRepository repository;

    /**
     * Constructs a new MedicationService with the specified repository.
     * 
     * @param repository the MedicationRepository used for data access
     */
    public MedicationService(MedicationRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all medications from the database.
     * 
     * @return list of all Medication entities
     */
    public List<Medication> getAllMedications() {
        return repository.findAll();
    }

    /**
     * Retrieves a medication by its unique identifier.
     * 
     * @param id the unique identifier of the medication
     * @return the Medication entity with the specified ID
     * @throws RuntimeException if medication with the given ID is not found
     */
    public Medication getMedicationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
    }

    /**
     * Adds one or more new medication records to the database.
     * 
     * @param medications list of Medication entities to be saved
     * @return list of saved Medication entities with generated IDs
     */
    public List<Medication> addMedication(List<Medication> medications) {
        return repository.saveAll(medications);
    }

    /**
     * Updates one or more existing medication records in the database.
     * 
     * @param medications list of Medication entities with updated information
     * @return list of updated Medication entities
     */
    public List<Medication> updateMedication(List<Medication> medications) {
        return repository.saveAll(medications);
    }

    /**
     * Performs a soft delete by marking a medication as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the medication to soft delete
     * @return the updated Medication entity with isDisabled set to true
     * @throws RuntimeException if medication with the given ID is not found
     */
    public Medication softDeleteMedication(Long id) {
        Medication medication = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
        medication.setDisabled(true);
        return repository.save(medication);
    }

    /**
     * Permanently deletes a medication record from the database.
     * 
     * @param id the unique identifier of the medication to delete
     * @throws RuntimeException if medication with the given ID is not found
     */
    public void deleteMedication(Long id) {
        Medication medication = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
        repository.delete(medication);
    }
}
