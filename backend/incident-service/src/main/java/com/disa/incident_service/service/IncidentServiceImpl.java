/*
 * Incident Service Implementation
 *
 * Implements the IncidentService interface.
 * Handles business logic for incident management and publishes events to RabbitMQ.
 */

package com.disa.incident_service.service;

import com.disa.incident_service.entity.Incident;
import com.disa.incident_service.entity.IncidentStatus;
import com.disa.incident_service.entity.Severity;
import com.disa.incident_service.event.EventPublisher;
import com.disa.incident_service.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final EventPublisher eventPublisher;

    @Override
    public Incident createIncident(Incident incident) {
        // Generate incident code
        long count = incidentRepository.count() + 1;
        incident.setIncidentCode(String.format("INC-%03d", count));
        incident.setStatus(IncidentStatus.REPORTED);

        Incident saved = incidentRepository.save(incident);

        // Publish event
        eventPublisher.publishIncidentCreated(saved);

        return saved;
    }

    @Override
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }

    @Override
    public Incident updateIncident(Long id, Incident incident) {
        Incident existing = getIncidentById(id);
        existing.setType(incident.getType());
        existing.setSeverity(incident.getSeverity());
        existing.setDescription(incident.getDescription());
        existing.setLatitude(incident.getLatitude());
        existing.setLongitude(incident.getLongitude());
        existing.setAddress(incident.getAddress());
        return incidentRepository.save(existing);
    }

    @Override
    public Incident escalateIncident(Long id) {
        Incident incident = getIncidentById(id);
        Severity previousSeverity = incident.getSeverity();

        // Escalate severity
        Severity[] severities = Severity.values();
        int currentIndex = incident.getSeverity().ordinal();
        if (currentIndex < severities.length - 1) {
            incident.setSeverity(severities[currentIndex + 1]);
        }

        Incident saved = incidentRepository.save(incident);

        // Publish event
        eventPublisher.publishIncidentEscalated(saved, previousSeverity);

        return saved;
    }

    @Override
    public Incident updateIncidentStatus(Long id, IncidentStatus status) {
        Incident incident = getIncidentById(id);
        incident.setStatus(status);
        return incidentRepository.save(incident);
    }

    @Override
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }
}
