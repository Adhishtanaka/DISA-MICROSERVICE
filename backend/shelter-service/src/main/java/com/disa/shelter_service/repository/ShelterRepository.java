/**
 * ShelterRepository.java
 *
 * Spring Data JPA repository interface for the Shelter entity.
 * Provides standard CRUD operations inherited from JpaRepository,
 * along with custom query methods for shelter-specific lookups.
 *
 * Custom methods:
 *   - findByStatusIn(List<ShelterStatus>)    : Retrieves shelters matching any of the given statuses
 *   - findByShelterCode(String)              : Retrieves a shelter by its unique shelter code
 *   - findByStatus(ShelterStatus)            : Retrieves all shelters with a specific status
 */
package com.disa.shelter_service.repository;

import com.disa.shelter_service.entity.Shelter;
import com.disa.shelter_service.entity.ShelterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    List<Shelter> findByStatusIn(List<ShelterStatus> statuses);

    Optional<Shelter> findByShelterCode(String shelterCode);

    List<Shelter> findByStatus(ShelterStatus status);
}
