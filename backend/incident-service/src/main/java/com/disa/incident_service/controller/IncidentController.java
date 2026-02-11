/*
 * Incident Controller
 *
 * REST controller that provides endpoints for managing disaster incidents.
 * Handles CRUD operations, incident escalation, and status updates.
 * Publishes events to RabbitMQ when incidents are created or escalated.
 */

package com.disa.incident_service.controller;

import com.disa.incident_service.dto.IncidentRequest;
import com.disa.incident_service.dto.IncidentResponse;
import com.disa.incident_service.entity.Incident;
import com.disa.incident_service.entity.IncidentStatus;
import com.disa.incident_service.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(@Valid @RequestBody IncidentRequest request) {
        Incident incident = new Incident();
        incident.setType(request.getType());
        incident.setSeverity(request.getSeverity());
        incident.setDescription(request.getDescription());
        incident.setLatitude(request.getLatitude());
        incident.setLongitude(request.getLongitude());
        incident.setAddress(request.getAddress());

        Incident saved = incidentService.createIncident(incident);
        return ResponseEntity.ok(mapToResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> getAllIncidents() {
        List<Incident> incidents = incidentService.getAllIncidents();
        List<IncidentResponse> responses = incidents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getIncidentById(@PathVariable Long id) {
        Incident incident = incidentService.getIncidentById(id);
        return ResponseEntity.ok(mapToResponse(incident));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponse> updateIncident(@PathVariable Long id, @Valid @RequestBody IncidentRequest request) {
        Incident incident = new Incident();
        incident.setType(request.getType());
        incident.setSeverity(request.getSeverity());
        incident.setDescription(request.getDescription());
        incident.setLatitude(request.getLatitude());
        incident.setLongitude(request.getLongitude());
        incident.setAddress(request.getAddress());

        Incident updated = incidentService.updateIncident(id, incident);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @PutMapping("/{id}/escalate")
    public ResponseEntity<IncidentResponse> escalateIncident(@PathVariable Long id) {
        Incident escalated = incidentService.escalateIncident(id);
        return ResponseEntity.ok(mapToResponse(escalated));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<IncidentResponse> updateIncidentStatus(@PathVariable Long id, @RequestBody IncidentStatus status) {
        Incident updated = incidentService.updateIncidentStatus(id, status);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }

    private IncidentResponse mapToResponse(Incident incident) {
        IncidentResponse response = new IncidentResponse();
        response.setId(incident.getId());
        response.setIncidentCode(incident.getIncidentCode());
        response.setType(incident.getType());
        response.setSeverity(incident.getSeverity());
        response.setStatus(incident.getStatus());
        response.setDescription(incident.getDescription());
        response.setLatitude(incident.getLatitude());
        response.setLongitude(incident.getLongitude());
        response.setAddress(incident.getAddress());
        response.setReportedAt(incident.getReportedAt());
        response.setUpdatedAt(incident.getUpdatedAt());
        return response;
    }
}
