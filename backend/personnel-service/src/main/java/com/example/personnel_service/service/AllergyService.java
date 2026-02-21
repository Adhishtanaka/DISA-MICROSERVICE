package com.example.personnel_service.service;

import com.example.personnel_service.entity.Allergy;
import com.example.personnel_service.repository.AllergyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Allergy entities.
 * Provides business logic for Allergy CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class AllergyService {
    private final AllergyRepository _allergyRepository;

    /**
     * Constructs a new AllergyService with the specified repository.
     * 
     * @param allergyRepository the AllergyRepository used for data access
     */
    public AllergyService(AllergyRepository allergyRepository) {
        this._allergyRepository = allergyRepository;
    }

    /**
     * Retrieves all allergies from the database.
     * 
     * @return list of all Allergy entities
     */
    public List<Allergy> getAllAllergies() {
        return _allergyRepository.findAll();
    }

    /**
     * Retrieves an allergy by its unique identifier.
     * 
     * @param id the unique identifier of the allergy
     * @return the Allergy entity with the specified ID
     * @throws RuntimeException if allergy with the given ID is not found
     */
    public Allergy getAllergyById(Long id) {
        return _allergyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allergy not found"));
    }

    /**
     * Adds one or more new allergy records to the database.
     * 
     * @param allergy list of Allergy entities to be saved
     * @return list of saved Allergy entities with generated IDs
     */
    public List<Allergy> addAllergy(List<Allergy> allergy) {
        return _allergyRepository.saveAll(allergy);
    }

    /**
     * Updates one or more existing allergy records in the database.
     * 
     * @param allergy list of Allergy entities with updated information
     * @return list of updated Allergy entities
     */
    public List<Allergy> updateAllergy(List<Allergy> allergy) {
        return _allergyRepository.saveAll(allergy);
    }

    /**
     * Performs a soft delete by marking an allergy as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the allergy to soft delete
     * @return the updated Allergy entity with isDisabled set to true
     * @throws RuntimeException if allergy with the given ID is not found
     */
    public Allergy softDeleteAllergy(Long id) {
        Allergy allergy = _allergyRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Allergy not found"));
        allergy.setDisabled(true);
        return _allergyRepository.save(allergy);
    }

    /**
     * Permanently deletes an allergy record from the database.
     * 
     * @param id the unique identifier of the allergy to delete
     * @throws RuntimeException if allergy with the given ID is not found
     */
    public void deleteAllergy(long id) {
        Allergy allergy = _allergyRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Allergy not found"));
        _allergyRepository.delete(allergy);
    }
}
