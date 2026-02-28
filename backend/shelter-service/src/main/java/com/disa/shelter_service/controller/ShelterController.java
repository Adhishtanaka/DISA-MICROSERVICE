/**
 * ShelterController.java
 *
 * REST controller for the Shelter Service, exposing HTTP endpoints for managing
 * emergency shelters within the DISA platform.
 *
 * Base URL: /api/shelters
 *
 * Endpoints:
 *   POST   /api/shelters              - Create a new shelter
 *   GET    /api/shelters              - Retrieve all shelters
 *   GET    /api/shelters/{id}         - Retrieve a shelter by ID
 *   GET    /api/shelters/available    - Retrieve all available shelters
 *   GET    /api/shelters/nearby       - Retrieve shelters within a given radius
 *   PUT    /api/shelters/{id}         - Update shelter details
 *   POST   /api/shelters/{id}/checkin  - Check in a number of people to a shelter
 *   POST   /api/shelters/{id}/checkout - Check out a number of people from a shelter
 *   PUT    /api/shelters/{id}/status  - Update the operational status of a shelter
 *   DELETE /api/shelters/{id}         - Delete a shelter
 */
package com.disa.shelter_service.controller;

import com.disa.shelter_service.dto.CheckInRequest;
import com.disa.shelter_service.dto.CheckOutRequest;
import com.disa.shelter_service.dto.ShelterRequest;
import com.disa.shelter_service.entity.Shelter;
import com.disa.shelter_service.entity.ShelterStatus;
import com.disa.shelter_service.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Shelter Management", description = "APIs for managing emergency shelters in the DISA platform")
@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @Operation(summary = "Create a new shelter", description = "Registers a new emergency shelter in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Shelter created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<Shelter> createShelter(@Valid @RequestBody ShelterRequest request) {
        Shelter shelter = shelterService.createShelter(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(shelter);
    }

    @Operation(summary = "Get all shelters", description = "Retrieves a list of all registered shelters")
    @ApiResponse(responseCode = "200", description = "List of shelters returned successfully")
    @GetMapping
    public ResponseEntity<List<Shelter>> getAllShelters() {
        return ResponseEntity.ok(shelterService.getAllShelters());
    }

    @Operation(summary = "Get shelter by ID", description = "Retrieves a specific shelter by its unique ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Shelter found"),
        @ApiResponse(responseCode = "400", description = "Shelter not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Shelter> getShelterById(
            @Parameter(description = "Unique ID of the shelter") @PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getShelterById(id));
    }

    @Operation(summary = "Get available shelters", description = "Retrieves shelters with status OPERATIONAL or UNDER_PREPARATION")
    @ApiResponse(responseCode = "200", description = "List of available shelters returned")
    @GetMapping("/available")
    public ResponseEntity<List<Shelter>> getAvailableShelters() {
        return ResponseEntity.ok(shelterService.getAvailableShelters());
    }

    @Operation(summary = "Get nearby shelters", description = "Retrieves shelters within a specified radius (km) of a geographic coordinate")
    @ApiResponse(responseCode = "200", description = "List of nearby shelters returned")
    @GetMapping("/nearby")
    public ResponseEntity<List<Shelter>> getNearbyShelters(
            @Parameter(description = "Latitude of the search origin") @RequestParam Double latitude,
            @Parameter(description = "Longitude of the search origin") @RequestParam Double longitude,
            @Parameter(description = "Search radius in kilometres (default: 50)") @RequestParam(defaultValue = "50") Double radiusKm) {
        return ResponseEntity.ok(shelterService.getNearbyShelters(latitude, longitude, radiusKm));
    }

    @Operation(summary = "Update shelter", description = "Updates the details of an existing shelter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Shelter updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or shelter not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Shelter> updateShelter(
            @Parameter(description = "Unique ID of the shelter") @PathVariable Long id,
            @Valid @RequestBody ShelterRequest request) {
        return ResponseEntity.ok(shelterService.updateShelter(id, request));
    }

    @Operation(summary = "Check in people", description = "Registers a number of people checking into the shelter, updating occupancy")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check-in successful"),
        @ApiResponse(responseCode = "400", description = "Insufficient capacity or shelter not found")
    })
    @PostMapping("/{id}/checkin")
    public ResponseEntity<Shelter> checkIn(
            @Parameter(description = "Unique ID of the shelter") @PathVariable Long id,
            @Valid @RequestBody CheckInRequest request) {
        Shelter shelter = shelterService.checkIn(id, request.getNumberOfPeople());
        return ResponseEntity.ok(shelter);
    }

    @Operation(summary = "Check out people", description = "Registers a number of people checking out of the shelter, updating occupancy")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check-out successful"),
        @ApiResponse(responseCode = "400", description = "Invalid count or shelter not found")
    })
    @PostMapping("/{id}/checkout")
    public ResponseEntity<Shelter> checkOut(
            @Parameter(description = "Unique ID of the shelter") @PathVariable Long id,
            @Valid @RequestBody CheckOutRequest request) {
        Shelter shelter = shelterService.checkOut(id, request.getNumberOfPeople());
        return ResponseEntity.ok(shelter);
    }

    @Operation(summary = "Update shelter status", description = "Manually updates the operational status of a shelter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Shelter not found")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<Shelter> updateStatus(
            @Parameter(description = "Unique ID of the shelter") @PathVariable Long id,
            @Parameter(description = "New status: OPERATIONAL, FULL, CLOSED, or UNDER_PREPARATION") @RequestParam ShelterStatus status) {
        return ResponseEntity.ok(shelterService.updateStatus(id, status));
    }

    @Operation(summary = "Delete shelter", description = "Permanently removes a shelter from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Shelter deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Shelter not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelter(
            @Parameter(description = "Unique ID of the shelter") @PathVariable Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.noContent().build();
    }
}
