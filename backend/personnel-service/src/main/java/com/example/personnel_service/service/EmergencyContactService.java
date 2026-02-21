package com.example.personnel_service.service;

import com.example.personnel_service.entity.EmergencyContact;
import com.example.personnel_service.repository.EmergencyContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing EmergencyContact entities.
 * Provides business logic for EmergencyContact CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class EmergencyContactService {
    private final EmergencyContactRepository repository;

    /**
     * Constructs a new EmergencyContactService with the specified repository.
     * 
     * @param repository the EmergencyContactRepository used for data access
     */
    public EmergencyContactService(EmergencyContactRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all emergency contacts from the database.
     * 
     * @return list of all EmergencyContact entities
     */
    public List<EmergencyContact> getAllEmergencyContacts() {
        return repository.findAll();
    }

    /**
     * Retrieves an emergency contact by its unique identifier.
     * 
     * @param id the unique identifier of the emergency contact
     * @return the EmergencyContact entity with the specified ID
     * @throws RuntimeException if emergency contact with the given ID is not found
     */
    public EmergencyContact getEmergencyContactById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emergency contact not found"));
    }

    /**
     * Adds one or more new emergency contact records to the database.
     * 
     * @param emergencyContacts list of EmergencyContact entities to be saved
     * @return list of saved EmergencyContact entities with generated IDs
     */
    public List<EmergencyContact> addEmergencyContact(List<EmergencyContact> emergencyContacts) {
        return repository.saveAll(emergencyContacts);
    }

    /**
     * Updates one or more existing emergency contact records in the database.
     * 
     * @param emergencyContacts list of EmergencyContact entities with updated information
     * @return list of updated EmergencyContact entities
     */
    public List<EmergencyContact> updateEmergencyContact(List<EmergencyContact> emergencyContacts) {
        return repository.saveAll(emergencyContacts);
    }

    /**
     * Performs a soft delete by marking an emergency contact as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the emergency contact to soft delete
     * @return the updated EmergencyContact entity with isDisabled set to true
     * @throws RuntimeException if emergency contact with the given ID is not found
     */
    public EmergencyContact softDeleteEmergencyContact(Long id) {
        EmergencyContact emergencyContact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emergency contact not found"));
        emergencyContact.setDisabled(true);
        return repository.save(emergencyContact);
    }

    /**
     * Permanently deletes an emergency contact record from the database.
     * 
     * @param id the unique identifier of the emergency contact to delete
     * @throws RuntimeException if emergency contact with the given ID is not found
     */
    public void deleteEmergencyContact(Long id) {
        EmergencyContact emergencyContact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emergency contact not found"));
        repository.delete(emergencyContact);
    }
}
