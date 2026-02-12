/*
 * Incident Controller
 *
 * REST controller providing HTTP endpoints for incident management.
 * Handles CRUD operations and incident escalation with proper validation.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.controller;

import com.disa.incident_service.dto.EscalateRequest;
import com.disa.incident_service.dto.IncidentRequest;
import com.disa.incident_service.dto.IncidentResponse;
import com.disa.incident_service.entity.IncidentStatus;
import com.disa.incident_service.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(@Valid @RequestBody IncidentRequest request) {
        IncidentResponse response = incidentService.createIncident(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<IncidentResponse>> getAllIncidents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<IncidentResponse> incidents = incidentService.getAllIncidents(pageable);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getIncidentById(@PathVariable Long id) {
        Optional<IncidentResponse> incident = incidentService.getIncidentById(id);
        return incident.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponse> updateIncident(
            @PathVariable Long id,
            @Valid @RequestBody IncidentRequest request) {
        try {
            IncidentResponse response = incidentService.updateIncident(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/escalate")
    public ResponseEntity<IncidentResponse> escalateIncident(
            @PathVariable Long id,
            @Valid @RequestBody EscalateRequest request) {
        try {
            IncidentResponse response = incidentService.escalateIncident(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<IncidentResponse> updateIncidentStatus(
            @PathVariable Long id,
            @RequestParam IncidentStatus status) {
        try {
            IncidentResponse response = incidentService.updateIncidentStatus(id, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        try {
            incidentService.deleteIncident(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentResponse>> getIncidentsByStatus(@PathVariable IncidentStatus status) {
        List<IncidentResponse> incidents = incidentService.getIncidentsByStatus(status);
        return ResponseEntity.ok(incidents);
    }
}