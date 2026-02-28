package com.example.personnel_service.repository;

import com.example.personnel_service.entity.ChronicCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChronicConditionRepository extends JpaRepository<ChronicCondition, Long> {
}
