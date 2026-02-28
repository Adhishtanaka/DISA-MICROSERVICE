package com.example.personnel_service.repository;

import com.example.personnel_service.entity.PhysicalLimitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalLimitationRepository extends JpaRepository<PhysicalLimitation, Long> {
}
