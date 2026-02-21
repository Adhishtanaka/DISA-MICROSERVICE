package com.example.personnel_service.service;

import com.example.personnel_service.entity.InjuryHistory;
import com.example.personnel_service.repository.InjuryHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing InjuryHistory entities.
 * Provides business logic for InjuryHistory CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class InjuryService {
    private final InjuryHistoryRepository repository;

    /**
     * Constructs a new InjuryService with the specified repository.
     * 
     * @param repository the InjuryHistoryRepository used for data access
     */
    public InjuryService(InjuryHistoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all injury histories from the database.
     * 
     * @return list of all InjuryHistory entities
     */
    public List<InjuryHistory> getAllInjuryHistories() {
        return repository.findAll();
    }

    /**
     * Retrieves an injury history by its unique identifier.
     * 
     * @param id the unique identifier of the injury history
     * @return the InjuryHistory entity with the specified ID
     * @throws RuntimeException if injury history with the given ID is not found
     */
    public InjuryHistory getInjuryHistoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Injury history not found"));
    }

    /**
     * Adds one or more new injury history records to the database.
     * 
     * @param injuryHistories list of InjuryHistory entities to be saved
     * @return list of saved InjuryHistory entities with generated IDs
     */
    public List<InjuryHistory> addInjuryHistory(List<InjuryHistory> injuryHistories) {
        return repository.saveAll(injuryHistories);
    }

    /**
     * Updates one or more existing injury history records in the database.
     * 
     * @param injuryHistories list of InjuryHistory entities with updated information
     * @return list of updated InjuryHistory entities
     */
    public List<InjuryHistory> updateInjuryHistory(List<InjuryHistory> injuryHistories) {
        return repository.saveAll(injuryHistories);
    }

    /**
     * Performs a soft delete by marking an injury history as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the injury history to soft delete
     * @return the updated InjuryHistory entity with isDisabled set to true
     * @throws RuntimeException if injury history with the given ID is not found
     */
    public InjuryHistory softDeleteInjuryHistory(Long id) {
        InjuryHistory injuryHistory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Injury history not found"));
        injuryHistory.setDisabled(true);
        return repository.save(injuryHistory);
    }

    /**
     * Permanently deletes an injury history record from the database.
     * 
     * @param id the unique identifier of the injury history to delete
     * @throws RuntimeException if injury history with the given ID is not found
     */
    public void deleteInjuryHistory(Long id) {
        InjuryHistory injuryHistory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Injury history not found"));
        repository.delete(injuryHistory);
    }
}
