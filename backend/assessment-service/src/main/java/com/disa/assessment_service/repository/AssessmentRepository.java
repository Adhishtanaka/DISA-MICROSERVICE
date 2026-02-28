package com.disa.assessment_service.repository;

import com.disa.assessment_service.entity.Assessment;
import com.disa.assessment_service.entity.AssessmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByIncidentId(Long incidentId);
    List<Assessment> findByStatus(AssessmentStatus status);
    List<Assessment> findByAssessorId(Long assessorId);
}
