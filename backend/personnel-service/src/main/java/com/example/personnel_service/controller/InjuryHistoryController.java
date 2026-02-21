package com.example.personnel_service.controller;

import com.example.personnel_service.entity.InjuryHistory;
import com.example.personnel_service.service.InjuryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing InjuryHistory entities.
 * Provides endpoints for CRUD operations on personnel injury history records.
 * Base path: /api/personnel/injury-histories
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/injury-histories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InjuryHistoryController {
    private final InjuryService injuryService;

    /**
     * Constructs a new InjuryHistoryController with the specified InjuryService.
     * 
     * @param injuryService the service used to handle InjuryHistory business logic
     */
    public InjuryHistoryController(InjuryService injuryService) {
        this.injuryService = injuryService;
    }

    /**
     * Retrieves all injury histories from the database.
     * 
     * @return ResponseEntity containing a list of all InjuryHistory entities
     */
    @GetMapping
    public ResponseEntity<List<InjuryHistory>> getAllInjuryHistories() {
        return ResponseEntity.ok(injuryService.getAllInjuryHistories());
    }

    /**
     * Retrieves a specific injury history by its unique identifier.
     * 
     * @param id the unique identifier of the injury history to retrieve
     * @return ResponseEntity containing the InjuryHistory entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InjuryHistory> getInjuryHistoryById(@PathVariable Long id) {
        return ResponseEntity.ok(injuryService.getInjuryHistoryById(id));
    }

    /**
     * Creates one or more new injury history records.
     * 
     * @param injuryHistories list of InjuryHistory entities to create
     * @return ResponseEntity containing the created InjuryHistory entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<InjuryHistory>> createInjuryHistories(@RequestBody List<InjuryHistory> injuryHistories) {
        return ResponseEntity.status(HttpStatus.CREATED).body(injuryService.addInjuryHistory(injuryHistories));
    }

    /**
     * Updates one or more existing injury history records.
     * 
     * @param injuryHistories list of InjuryHistory entities with updated information
     * @return ResponseEntity containing the updated InjuryHistory entities
     */
    @PutMapping
    public ResponseEntity<List<InjuryHistory>> updateInjuryHistories(@RequestBody List<InjuryHistory> injuryHistories) {
        return ResponseEntity.ok(injuryService.updateInjuryHistory(injuryHistories));
    }

    /**
     * Performs a soft delete on an injury history by marking it as disabled.
     * The injury history record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the injury history to soft delete
     * @return ResponseEntity containing the soft-deleted InjuryHistory entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<InjuryHistory> softDeleteInjuryHistory(@PathVariable Long id) {
        return ResponseEntity.ok(injuryService.softDeleteInjuryHistory(id));
    }

    /**
     * Permanently deletes an injury history record from the database.
     * 
     * @param id the unique identifier of the injury history to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInjuryHistory(@PathVariable Long id) {
        injuryService.deleteInjuryHistory(id);
        return ResponseEntity.noContent().build();
    }
}
