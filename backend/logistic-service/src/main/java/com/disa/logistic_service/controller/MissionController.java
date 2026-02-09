package com.disa.logistic_service.controller;

import com.disa.logistic_service.dto.MissionRequest;
import com.disa.logistic_service.dto.StatusUpdateRequest;
import com.disa.logistic_service.entity.Mission;
import com.disa.logistic_service.entity.MissionStatus;
import com.disa.logistic_service.entity.MissionType;
import com.disa.logistic_service.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller exposing Logistics endpoints.
 * Documented via Swagger @Tag and @Operation annotations.
 */
@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
@Tag(name = "Missions", description = "Endpoints for managing logistics missions")
@CrossOrigin(origins = "*") // Enable CORS for frontend
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    @Operation(summary = "Create Mission", description = "Manually creates a new logistics mission.")
    public ResponseEntity<Mission> createMission(@Valid @RequestBody MissionRequest request) {
        Mission mission = missionService.createMission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mission);
    }

    @GetMapping
    @Operation(summary = "Get All Missions", description = "Retrieves a list of all registered missions.")
    public ResponseEntity<List<Mission>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Mission by ID", description = "Retrieves details of a specific mission.")
    public ResponseEntity<Mission> getMissionById(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filter by Status", description = "Retrieves missions based on their current status.")
    public ResponseEntity<List<Mission>> getMissionsByStatus(@PathVariable MissionStatus status) {
        return ResponseEntity.ok(missionService.getMissionsByStatus(status));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Filter by Type", description = "Retrieves missions based on their mission type.")
    public ResponseEntity<List<Mission>> getMissionsByType(@PathVariable MissionType type) {
        return ResponseEntity.ok(missionService.getMissionsByType(type));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update Status", description = "Updates the status of an existing mission (e.g. PENDING to IN_PROGRESS).")
    public ResponseEntity<Mission> updateMissionStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        Mission mission = missionService.updateMissionStatus(id, request.getStatus());
        return ResponseEntity.ok(mission);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Mission", description = "Removes a mission from the system.")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }
}