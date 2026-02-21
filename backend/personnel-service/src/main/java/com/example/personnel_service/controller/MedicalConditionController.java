package com.example.personnel_service.controller;

import com.example.personnel_service.entity.MedicalCondition;
import com.example.personnel_service.service.MedicalConditionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing MedicalCondition entities.
 * Provides endpoints for CRUD operations on personnel medical condition records.
 * Base path: /api/personnel/medical-conditions
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/medical-conditions")
public class MedicalConditionController {
    private final MedicalConditionService medicalConditionService;

    /**
     * Constructs a new MedicalConditionController with the specified service.
     * 
     * @param medicalConditionService the service used to handle MedicalCondition business logic
     */
    public MedicalConditionController(MedicalConditionService medicalConditionService) {
        this.medicalConditionService = medicalConditionService;
    }

    /**
     * Retrieves all medical conditions from the database.
     * 
     * @return ResponseEntity containing a list of all MedicalCondition entities
     */
    @GetMapping
    public ResponseEntity<List<MedicalCondition>> getAllMedicalConditions() {
        return ResponseEntity.ok(medicalConditionService.getAllMedicalConditions());
    }

    /**
     * Retrieves a specific medical condition by its unique identifier.
     * 
     * @param id the unique identifier of the medical condition to retrieve
     * @return ResponseEntity containing the MedicalCondition entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalCondition> getMedicalConditionById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalConditionService.getMedicalConditionById(id));
    }

    /**
     * Creates one or more new medical condition records.
     * 
     * @param medicalConditions list of MedicalCondition entities to create
     * @return ResponseEntity containing the created MedicalCondition entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<MedicalCondition>> createMedicalConditions(@RequestBody List<MedicalCondition> medicalConditions) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalConditionService.addMedicalCondition(medicalConditions));
    }

    /**
     * Updates one or more existing medical condition records.
     * 
     * @param medicalConditions list of MedicalCondition entities with updated information
     * @return ResponseEntity containing the updated MedicalCondition entities
     */
    @PutMapping
    public ResponseEntity<List<MedicalCondition>> updateMedicalConditions(@RequestBody List<MedicalCondition> medicalConditions) {
        return ResponseEntity.ok(medicalConditionService.updateMedicalCondition(medicalConditions));
    }

    /**
     * Performs a soft delete on a medical condition by marking it as disabled.
     * The medical condition record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the medical condition to soft delete
     * @return ResponseEntity containing the soft-deleted MedicalCondition entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<MedicalCondition> softDeleteMedicalCondition(@PathVariable Long id) {
        return ResponseEntity.ok(medicalConditionService.softDeleteMedicalCondition(id));
    }

    /**
     * Permanently deletes a medical condition record from the database.
     * 
     * @param id the unique identifier of the medical condition to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalCondition(@PathVariable Long id) {
        medicalConditionService.deleteMedicalCondition(id);
        return ResponseEntity.noContent().build();
    }
}
