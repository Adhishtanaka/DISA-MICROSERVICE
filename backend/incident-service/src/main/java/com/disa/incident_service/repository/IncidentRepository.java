/*
 * Incident Repository
 *
 * JPA repository interface for Incident entity.
 * Provides database operations for incident management.
 */

package com.disa.incident_service.repository;

import com.disa.incident_service.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
}
