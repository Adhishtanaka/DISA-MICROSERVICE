package com.disa.assessment_service.service;

import com.disa.assessment_service.dto.AssessmentRequest;
import com.disa.assessment_service.dto.AssessmentResponse;
import com.disa.assessment_service.entity.Assessment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssessmentService {
    Assessment createAssessment(AssessmentRequest request);
    Assessment getAssessmentById(Long id);
    List<Assessment> getAllAssessments();
    List<Assessment> getAssessmentsByIncident(Long incidentId);
    List<Assessment> getCompletedAssessments();
    List<Assessment> getAssessmentsByAssessor(Long assessorId);
    Assessment updateAssessment(Long id, AssessmentRequest request);
    Assessment completeAssessment(Long id);
    Assessment uploadPhoto(Long assessmentId, MultipartFile file);
    void deleteAssessment(Long id);
}
