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
