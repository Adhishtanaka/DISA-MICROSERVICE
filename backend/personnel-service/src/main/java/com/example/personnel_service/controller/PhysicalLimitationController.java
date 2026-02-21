package com.example.personnel_service.controller;

import com.example.personnel_service.entity.PhysicalLimitation;
import com.example.personnel_service.service.PhysicalLimitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing PhysicalLimitation entities.
 * Provides endpoints for CRUD operations on personnel physical limitation records.
 * Base path: /api/personnel/physical-limitations
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/physical-limitations")
public class PhysicalLimitationController {
    private final PhysicalLimitationService physicalLimitationService;

    /**
     * Constructs a new PhysicalLimitationController with the specified service.
     * 
     * @param physicalLimitationService the service used to handle PhysicalLimitation business logic
     */
    public PhysicalLimitationController(PhysicalLimitationService physicalLimitationService) {
        this.physicalLimitationService = physicalLimitationService;
    }

    /**
     * Retrieves all physical limitations from the database.
     * 
     * @return ResponseEntity containing a list of all PhysicalLimitation entities
     */
    @GetMapping
    public ResponseEntity<List<PhysicalLimitation>> getAllPhysicalLimitations() {
        return ResponseEntity.ok(physicalLimitationService.getAllPhysicalLimitations());
    }

    /**
     * Retrieves a specific physical limitation by its unique identifier.
     * 
     * @param id the unique identifier of the physical limitation to retrieve
     * @return ResponseEntity containing the PhysicalLimitation entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PhysicalLimitation> getPhysicalLimitationById(@PathVariable Long id) {
        return ResponseEntity.ok(physicalLimitationService.getPhysicalLimitationById(id));
    }

    /**
     * Creates one or more new physical limitation records.
     * 
     * @param physicalLimitations list of PhysicalLimitation entities to create
     * @return ResponseEntity containing the created PhysicalLimitation entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<PhysicalLimitation>> createPhysicalLimitations(@RequestBody List<PhysicalLimitation> physicalLimitations) {
        return ResponseEntity.status(HttpStatus.CREATED).body(physicalLimitationService.addPhysicalLimitation(physicalLimitations));
    }

    /**
     * Updates one or more existing physical limitation records.
     * 
     * @param physicalLimitations list of PhysicalLimitation entities with updated information
     * @return ResponseEntity containing the updated PhysicalLimitation entities
     */
    @PutMapping
    public ResponseEntity<List<PhysicalLimitation>> updatePhysicalLimitations(@RequestBody List<PhysicalLimitation> physicalLimitations) {
        return ResponseEntity.ok(physicalLimitationService.updatePhysicalLimitation(physicalLimitations));
    }

    /**
     * Performs a soft delete on a physical limitation by marking it as disabled.
     * The physical limitation record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the physical limitation to soft delete
     * @return ResponseEntity containing the soft-deleted PhysicalLimitation entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<PhysicalLimitation> softDeletePhysicalLimitation(@PathVariable Long id) {
        return ResponseEntity.ok(physicalLimitationService.softDeletePhysicalLimitation(id));
    }

    /**
     * Permanently deletes a physical limitation record from the database.
     * 
     * @param id the unique identifier of the physical limitation to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhysicalLimitation(@PathVariable Long id) {
        physicalLimitationService.deletePhysicalLimitation(id);
        return ResponseEntity.noContent().build();
    }
}
