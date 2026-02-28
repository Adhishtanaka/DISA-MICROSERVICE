package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Allergy;
import com.example.personnel_service.service.AllergyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Allergy entities.
 * Provides endpoints for CRUD operations on personnel allergy records.
 * Base path: /api/personnel/allergies
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/allergies")
public class AllergyController {
    private final AllergyService allergyService;

    /**
     * Constructs a new AllergyController with the specified AllergyService.
     * 
     * @param allergyService the service used to handle Allergy business logic
     */
    public AllergyController(AllergyService allergyService) {
        this.allergyService = allergyService;
    }

    /**
     * Retrieves all allergies from the database.
     * 
     * @return ResponseEntity containing a list of all Allergy entities
     */
    @GetMapping
    public ResponseEntity<List<Allergy>> getAllAllergies() {
        return ResponseEntity.ok(allergyService.getAllAllergies());
    }

    /**
     * Retrieves a specific allergy by its unique identifier.
     * 
     * @param id the unique identifier of the allergy to retrieve
     * @return ResponseEntity containing the Allergy entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Allergy> getAllergyById(@PathVariable Long id) {
        return ResponseEntity.ok(allergyService.getAllergyById(id));
    }

    /**
     * Creates one or more new allergy records.
     * 
     * @param allergies list of Allergy entities to create
     * @return ResponseEntity containing the created Allergy entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<Allergy>> createAllergies(@RequestBody List<Allergy> allergies) {
        return ResponseEntity.status(HttpStatus.CREATED).body(allergyService.addAllergy(allergies));
    }

    /**
     * Updates one or more existing allergy records.
     * 
     * @param allergies list of Allergy entities with updated information
     * @return ResponseEntity containing the updated Allergy entities
     */
    @PutMapping
    public ResponseEntity<List<Allergy>> updateAllergies(@RequestBody List<Allergy> allergies) {
        return ResponseEntity.ok(allergyService.updateAllergy(allergies));
    }

    /**
     * Performs a soft delete on an allergy by marking it as disabled.
     * The allergy record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the allergy to soft delete
     * @return ResponseEntity containing the soft-deleted Allergy entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Allergy> softDeleteAllergy(@PathVariable Long id) {
        return ResponseEntity.ok(allergyService.softDeleteAllergy(id));
    }

    /**
     * Permanently deletes an allergy record from the database.
     * 
     * @param id the unique identifier of the allergy to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllergy(@PathVariable Long id) {
        allergyService.deleteAllergy(id);
        return ResponseEntity.noContent().build();
    }
}
