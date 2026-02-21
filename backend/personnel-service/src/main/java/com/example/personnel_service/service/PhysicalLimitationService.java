package com.example.personnel_service.service;

import com.example.personnel_service.entity.PhysicalLimitation;
import com.example.personnel_service.repository.PhysicalLimitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing PhysicalLimitation entities.
 * Provides business logic for PhysicalLimitation CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class PhysicalLimitationService {
    private final PhysicalLimitationRepository repository;

    /**
     * Constructs a new PhysicalLimitationService with the specified repository.
     * 
     * @param repository the PhysicalLimitationRepository used for data access
     */
    public PhysicalLimitationService(PhysicalLimitationRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all physical limitations from the database.
     * 
     * @return list of all PhysicalLimitation entities
     */
    public List<PhysicalLimitation> getAllPhysicalLimitations() {
        return repository.findAll();
    }

    /**
     * Retrieves a physical limitation by its unique identifier.
     * 
     * @param id the unique identifier of the physical limitation
     * @return the PhysicalLimitation entity with the specified ID
     * @throws RuntimeException if physical limitation with the given ID is not found
     */
    public PhysicalLimitation getPhysicalLimitationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Physical limitation not found"));
    }

    /**
     * Adds one or more new physical limitation records to the database.
     * 
     * @param physicalLimitations list of PhysicalLimitation entities to be saved
     * @return list of saved PhysicalLimitation entities with generated IDs
     */
    public List<PhysicalLimitation> addPhysicalLimitation(List<PhysicalLimitation> physicalLimitations) {
        return repository.saveAll(physicalLimitations);
    }

    /**
     * Updates one or more existing physical limitation records in the database.
     * 
     * @param physicalLimitations list of PhysicalLimitation entities with updated information
     * @return list of updated PhysicalLimitation entities
     */
    public List<PhysicalLimitation> updatePhysicalLimitation(List<PhysicalLimitation> physicalLimitations) {
        return repository.saveAll(physicalLimitations);
    }

    /**
     * Performs a soft delete by marking a physical limitation as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the physical limitation to soft delete
     * @return the updated PhysicalLimitation entity with isDisabled set to true
     * @throws RuntimeException if physical limitation with the given ID is not found
     */
    public PhysicalLimitation softDeletePhysicalLimitation(Long id) {
        PhysicalLimitation physicalLimitation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Physical limitation not found"));
        physicalLimitation.setDisabled(true);
        return repository.save(physicalLimitation);
    }

    /**
     * Permanently deletes a physical limitation record from the database.
     * 
     * @param id the unique identifier of the physical limitation to delete
     * @throws RuntimeException if physical limitation with the given ID is not found
     */
    public void deletePhysicalLimitation(Long id) {
        PhysicalLimitation physicalLimitation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Physical limitation not found"));
        repository.delete(physicalLimitation);
    }
}
