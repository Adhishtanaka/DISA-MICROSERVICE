package com.example.personnel_service.repository;

import com.example.personnel_service.entity.MedicalCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalConditionRepository extends JpaRepository<MedicalCondition, Long> {
}
