/*
 * Incident Service Interface
 *
 * Defines the contract for incident business logic operations.
 * Includes CRUD operations and incident management functionality.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.service;

import com.disa.incident_service.dto.EscalateRequest;
import com.disa.incident_service.dto.IncidentRequest;
import com.disa.incident_service.dto.IncidentResponse;
import com.disa.incident_service.entity.IncidentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IncidentService {
    IncidentResponse createIncident(IncidentRequest request);
    Page<IncidentResponse> getAllIncidents(Pageable pageable);
    Optional<IncidentResponse> getIncidentById(Long id);
    IncidentResponse updateIncident(Long id, IncidentRequest request);
    IncidentResponse escalateIncident(Long id, EscalateRequest request);
    IncidentResponse updateIncidentStatus(Long id, IncidentStatus status);
    void deleteIncident(Long id);
    List<IncidentResponse> getIncidentsByStatus(IncidentStatus status);
}