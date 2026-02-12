/*
 * Incident Service Implementation
 *
 * Implements the IncidentService interface providing business logic
 * for incident management including CRUD operations and event publishing.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.service;

import com.disa.incident_service.dto.EscalateRequest;
import com.disa.incident_service.dto.IncidentRequest;
import com.disa.incident_service.dto.IncidentResponse;
import com.disa.incident_service.entity.Incident;
import com.disa.incident_service.entity.IncidentStatus;
import com.disa.incident_service.entity.Severity;
import com.disa.incident_service.event.EventPublisher;
import com.disa.incident_service.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final EventPublisher eventPublisher;

    @Override
    public IncidentResponse createIncident(IncidentRequest request) {
        // Generate incident code
        String incidentCode = generateIncidentCode();

        Incident incident = new Incident();
        incident.setIncidentCode(incidentCode);
        incident.setType(request.getType());
        incident.setSeverity(request.getSeverity());
        incident.setStatus(IncidentStatus.REPORTED);
        incident.setDescription(request.getDescription());
        incident.setLatitude(request.getLatitude());
        incident.setLongitude(request.getLongitude());
        incident.setAddress(request.getAddress());

        Incident savedIncident = incidentRepository.save(incident);

        // Publish event
        eventPublisher.publishIncidentCreated(savedIncident);

        return mapToResponse(savedIncident);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncidentResponse> getAllIncidents(Pageable pageable) {
        return incidentRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IncidentResponse> getIncidentById(Long id) {
        return incidentRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    public IncidentResponse updateIncident(Long id, IncidentRequest request) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found"));

        incident.setType(request.getType());
        incident.setSeverity(request.getSeverity());
        incident.setDescription(request.getDescription());
        incident.setLatitude(request.getLatitude());
        incident.setLongitude(request.getLongitude());
        incident.setAddress(request.getAddress());

        Incident updatedIncident = incidentRepository.save(incident);
        return mapToResponse(updatedIncident);
    }

    @Override
    public IncidentResponse escalateIncident(Long id, EscalateRequest request) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found"));

        Severity previousSeverity = incident.getSeverity();
        incident.setSeverity(request.getNewSeverity());

        Incident updatedIncident = incidentRepository.save(incident);

        // Publish escalation event
        eventPublisher.publishIncidentEscalated(updatedIncident, previousSeverity);

        return mapToResponse(updatedIncident);
    }

    @Override
    public IncidentResponse updateIncidentStatus(Long id, IncidentStatus status) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found"));

        incident.setStatus(status);
        Incident updatedIncident = incidentRepository.save(incident);

        return mapToResponse(updatedIncident);
    }

    @Override
    public void deleteIncident(Long id) {
        if (!incidentRepository.existsById(id)) {
            throw new RuntimeException("Incident not found");
        }
        incidentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> getIncidentsByStatus(IncidentStatus status) {
        return incidentRepository.findByStatus(status).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private String generateIncidentCode() {
        long count = incidentRepository.count();
        return String.format("INC-%03d", count + 1);
    }

    private IncidentResponse mapToResponse(Incident incident) {
        return new IncidentResponse(
            incident.getId(),
            incident.getIncidentCode(),
            incident.getType(),
            incident.getSeverity(),
            incident.getStatus(),
            incident.getDescription(),
            incident.getLatitude(),
            incident.getLongitude(),
            incident.getAddress(),
            incident.getReportedAt(),
            incident.getUpdatedAt()
        );
    }
}