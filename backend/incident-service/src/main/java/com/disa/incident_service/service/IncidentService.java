/*
 * Incident Service Interface
 *
 * Defines the contract for incident management operations.
 * Includes CRUD operations, escalation, and status updates.
 */

package com.disa.incident_service.service;

import com.disa.incident_service.entity.Incident;
import com.disa.incident_service.entity.IncidentStatus;
import com.disa.incident_service.entity.Severity;

import java.util.List;

public interface IncidentService {
    Incident createIncident(Incident incident);
    List<Incident> getAllIncidents();
    Incident getIncidentById(Long id);
    Incident updateIncident(Long id, Incident incident);
    Incident escalateIncident(Long id);
    Incident updateIncidentStatus(Long id, IncidentStatus status);
    void deleteIncident(Long id);
}
