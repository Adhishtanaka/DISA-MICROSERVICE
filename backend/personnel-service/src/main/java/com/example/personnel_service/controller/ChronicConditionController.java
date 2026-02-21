package com.example.personnel_service.controller;

import com.example.personnel_service.entity.ChronicCondition;
import com.example.personnel_service.service.ChronicConditionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing ChronicCondition entities.
 * Provides endpoints for CRUD operations on personnel chronic condition records.
 * Base path: /api/personnel/chronic-conditions
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/chronic-conditions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChronicConditionController {
    private final ChronicConditionService chronicConditionService;

    /**
     * Constructs a new ChronicConditionController with the specified service.
     * 
     * @param chronicConditionService the service used to handle ChronicCondition business logic
     */
    public ChronicConditionController(ChronicConditionService chronicConditionService) {
        this.chronicConditionService = chronicConditionService;
    }

    /**
     * Retrieves all chronic conditions from the database.
     * 
     * @return ResponseEntity containing a list of all ChronicCondition entities
     */
    @GetMapping
    public ResponseEntity<List<ChronicCondition>> getAllChronicConditions() {
        return ResponseEntity.ok(chronicConditionService.getAllChronicConditions());
    }

    /**
     * Retrieves a specific chronic condition by its unique identifier.
     * 
     * @param id the unique identifier of the chronic condition to retrieve
     * @return ResponseEntity containing the ChronicCondition entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChronicCondition> getChronicConditionById(@PathVariable Long id) {
        return ResponseEntity.ok(chronicConditionService.getChronicConditionById(id));
    }

    /**
     * Creates one or more new chronic condition records.
     * 
     * @param chronicConditions list of ChronicCondition entities to create
     * @return ResponseEntity containing the created ChronicCondition entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<ChronicCondition>> createChronicConditions(@RequestBody List<ChronicCondition> chronicConditions) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chronicConditionService.addChronicCondition(chronicConditions));
    }

    /**
     * Updates one or more existing chronic condition records.
     * 
     * @param chronicConditions list of ChronicCondition entities with updated information
     * @return ResponseEntity containing the updated ChronicCondition entities
     */
    @PutMapping
    public ResponseEntity<List<ChronicCondition>> updateChronicConditions(@RequestBody List<ChronicCondition> chronicConditions) {
        return ResponseEntity.ok(chronicConditionService.updateChronicCondition(chronicConditions));
    }

    /**
     * Performs a soft delete on a chronic condition by marking it as disabled.
     * The chronic condition record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the chronic condition to soft delete
     * @return ResponseEntity containing the soft-deleted ChronicCondition entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ChronicCondition> softDeleteChronicCondition(@PathVariable Long id) {
        return ResponseEntity.ok(chronicConditionService.softDeleteChronicCondition(id));
    }

    /**
     * Permanently deletes a chronic condition record from the database.
     * 
     * @param id the unique identifier of the chronic condition to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChronicCondition(@PathVariable Long id) {
        chronicConditionService.deleteChronicCondition(id);
        return ResponseEntity.noContent().build();
    }
}
