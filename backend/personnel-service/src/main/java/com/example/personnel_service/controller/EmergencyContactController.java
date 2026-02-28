package com.example.personnel_service.controller;

import com.example.personnel_service.entity.EmergencyContact;
import com.example.personnel_service.service.EmergencyContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing EmergencyContact entities.
 * Provides endpoints for CRUD operations on personnel emergency contact records.
 * Base path: /api/personnel/emergency-contacts
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/emergency-contacts")
public class EmergencyContactController {
    private final EmergencyContactService emergencyContactService;

    /**
     * Constructs a new EmergencyContactController with the specified service.
     * 
     * @param emergencyContactService the service used to handle EmergencyContact business logic
     */
    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    /**
     * Retrieves all emergency contacts from the database.
     * 
     * @return ResponseEntity containing a list of all EmergencyContact entities
     */
    @GetMapping
    public ResponseEntity<List<EmergencyContact>> getAllEmergencyContacts() {
        return ResponseEntity.ok(emergencyContactService.getAllEmergencyContacts());
    }

    /**
     * Retrieves a specific emergency contact by its unique identifier.
     * 
     * @param id the unique identifier of the emergency contact to retrieve
     * @return ResponseEntity containing the EmergencyContact entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContact> getEmergencyContactById(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyContactService.getEmergencyContactById(id));
    }

    /**
     * Creates one or more new emergency contact records.
     * 
     * @param emergencyContacts list of EmergencyContact entities to create
     * @return ResponseEntity containing the created EmergencyContact entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<EmergencyContact>> createEmergencyContacts(@RequestBody List<EmergencyContact> emergencyContacts) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emergencyContactService.addEmergencyContact(emergencyContacts));
    }

    /**
     * Updates one or more existing emergency contact records.
     * 
     * @param emergencyContacts list of EmergencyContact entities with updated information
     * @return ResponseEntity containing the updated EmergencyContact entities
     */
    @PutMapping
    public ResponseEntity<List<EmergencyContact>> updateEmergencyContacts(@RequestBody List<EmergencyContact> emergencyContacts) {
        return ResponseEntity.ok(emergencyContactService.updateEmergencyContact(emergencyContacts));
    }

    /**
     * Performs a soft delete on an emergency contact by marking it as disabled.
     * The emergency contact record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the emergency contact to soft delete
     * @return ResponseEntity containing the soft-deleted EmergencyContact entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EmergencyContact> softDeleteEmergencyContact(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyContactService.softDeleteEmergencyContact(id));
    }

    /**
     * Permanently deletes an emergency contact record from the database.
     * 
     * @param id the unique identifier of the emergency contact to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Long id) {
        emergencyContactService.deleteEmergencyContact(id);
        return ResponseEntity.noContent().build();
    }
}
