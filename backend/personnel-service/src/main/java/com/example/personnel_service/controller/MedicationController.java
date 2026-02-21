package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Medication;
import com.example.personnel_service.service.MedicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Medication entities.
 * Provides endpoints for CRUD operations on personnel medication records.
 * Base path: /api/personnel/medications
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/medications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MedicationController {
    private final MedicationService medicationService;

    /**
     * Constructs a new MedicationController with the specified MedicationService.
     * 
     * @param medicationService the service used to handle Medication business logic
     */
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    /**
     * Retrieves all medications from the database.
     * 
     * @return ResponseEntity containing a list of all Medication entities
     */
    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    /**
     * Retrieves a specific medication by its unique identifier.
     * 
     * @param id the unique identifier of the medication to retrieve
     * @return ResponseEntity containing the Medication entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    /**
     * Creates one or more new medication records.
     * 
     * @param medications list of Medication entities to create
     * @return ResponseEntity containing the created Medication entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<Medication>> createMedications(@RequestBody List<Medication> medications) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicationService.addMedication(medications));
    }

    /**
     * Updates one or more existing medication records.
     * 
     * @param medications list of Medication entities with updated information
     * @return ResponseEntity containing the updated Medication entities
     */
    @PutMapping
    public ResponseEntity<List<Medication>> updateMedications(@RequestBody List<Medication> medications) {
        return ResponseEntity.ok(medicationService.updateMedication(medications));
    }

    /**
     * Performs a soft delete on a medication by marking it as disabled.
     * The medication record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the medication to soft delete
     * @return ResponseEntity containing the soft-deleted Medication entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Medication> softDeleteMedication(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.softDeleteMedication(id));
    }

    /**
     * Permanently deletes a medication record from the database.
     * 
     * @param id the unique identifier of the medication to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
