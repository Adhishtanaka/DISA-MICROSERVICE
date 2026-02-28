/*
 * Incident Repository
 *
 * JPA repository interface for Incident entity operations.
 * Provides methods for querying and managing incident data.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.repository;

import com.disa.incident_service.entity.Incident;
import com.disa.incident_service.entity.IncidentStatus;
import com.disa.incident_service.entity.IncidentType;
import com.disa.incident_service.entity.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByStatus(IncidentStatus status);

    List<Incident> findByType(IncidentType type);

    List<Incident> findBySeverity(Severity severity);

    @Query("SELECT i FROM Incident i WHERE i.latitude BETWEEN :minLat AND :maxLat AND i.longitude BETWEEN :minLng AND :maxLng")
    List<Incident> findByLocationBounds(@Param("minLat") Double minLat, @Param("maxLat") Double maxLat,
                                       @Param("minLng") Double minLng, @Param("maxLng") Double maxLng);

    long countByStatus(IncidentStatus status);
}