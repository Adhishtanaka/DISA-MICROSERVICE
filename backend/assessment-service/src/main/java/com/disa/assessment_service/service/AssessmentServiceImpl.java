package com.disa.assessment_service.service;

import com.disa.assessment_service.dto.AssessmentRequest;
import com.disa.assessment_service.entity.Assessment;
import com.disa.assessment_service.entity.AssessmentStatus;
import com.disa.assessment_service.event.EventPublisher;
import com.disa.assessment_service.repository.AssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {
    
    private final AssessmentRepository assessmentRepository;
    private final EventPublisher eventPublisher;
    private final FileStorageService fileStorageService;
    
    @Override
    @Transactional
    public Assessment createAssessment(AssessmentRequest request) {
        Assessment assessment = new Assessment();
        assessment.setIncidentId(request.getIncidentId());
        assessment.setAssessorId(request.getAssessorId());
        assessment.setAssessorName(request.getAssessorName());
        assessment.setSeverity(request.getSeverity());
        assessment.setFindings(request.getFindings());
        assessment.setRecommendations(request.getRecommendations());
        assessment.setRequiredActions(request.getRequiredActions());
        assessment.setEstimatedCasualties(request.getEstimatedCasualties());
        assessment.setEstimatedDisplaced(request.getEstimatedDisplaced());
        assessment.setAffectedInfrastructure(request.getAffectedInfrastructure());
        assessment.setStatus(request.getStatus() != null ? request.getStatus() : AssessmentStatus.DRAFT);
        assessment.setAssessmentCode(generateAssessmentCode());
        
        Assessment saved = assessmentRepository.save(assessment);
        log.info("Assessment created: {} for incident: {}", saved.getAssessmentCode(), saved.getIncidentId());
        
        return saved;
    }
    
    @Override
    public Assessment getAssessmentById(Long id) {
        return assessmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));
    }
    
    @Override
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }
    
    @Override
    public List<Assessment> getAssessmentsByIncident(Long incidentId) {
        return assessmentRepository.findByIncidentId(incidentId);
    }
    
    @Override
    public List<Assessment> getCompletedAssessments() {
        return assessmentRepository.findByStatus(AssessmentStatus.COMPLETED);
    }
    
    @Override
    public List<Assessment> getAssessmentsByAssessor(Long assessorId) {
        return assessmentRepository.findByAssessorId(assessorId);
    }
    
    @Override
    @Transactional
    public Assessment updateAssessment(Long id, AssessmentRequest request) {
        Assessment assessment = getAssessmentById(id);
        
        assessment.setAssessorName(request.getAssessorName());
        assessment.setSeverity(request.getSeverity());
        assessment.setFindings(request.getFindings());
        assessment.setRecommendations(request.getRecommendations());
        assessment.setRequiredActions(request.getRequiredActions());
        assessment.setEstimatedCasualties(request.getEstimatedCasualties());
        assessment.setEstimatedDisplaced(request.getEstimatedDisplaced());
        assessment.setAffectedInfrastructure(request.getAffectedInfrastructure());
        
        Assessment updated = assessmentRepository.save(assessment);
        log.info("Assessment updated: {}", updated.getAssessmentCode());
        
        return updated;
    }
    
    @Override
    @Transactional
    public Assessment completeAssessment(Long id) {
        Assessment assessment = getAssessmentById(id);
        
        if (assessment.getStatus() == AssessmentStatus.COMPLETED) {
            throw new RuntimeException("Assessment already completed");
        }
        
        assessment.setStatus(AssessmentStatus.COMPLETED);
        assessment.setCompletedAt(LocalDateTime.now());
        
        Assessment completed = assessmentRepository.save(assessment);
        
        // Publish event to trigger task creation
        eventPublisher.publishAssessmentCompleted(completed);
        
        log.info("Assessment completed and event published: {}", assessment.getAssessmentCode());
        
        return completed;
    }
    
    @Override
    @Transactional
    public Assessment uploadPhoto(Long assessmentId, MultipartFile file) {
        Assessment assessment = getAssessmentById(assessmentId);
        
        String filename = fileStorageService.storeFile(file);
        String photoUrl = "/uploads/" + filename;
        
        assessment.getPhotoUrls().add(photoUrl);
        Assessment updated = assessmentRepository.save(assessment);
        
        log.info("Photo uploaded for assessment: {}", assessment.getAssessmentCode());
        
        return updated;
    }
    
    @Override
    @Transactional
    public void deleteAssessment(Long id) {
        Assessment assessment = getAssessmentById(id);
        assessmentRepository.delete(assessment);
        log.info("Assessment deleted: {}", assessment.getAssessmentCode());
    }
    
    private String generateAssessmentCode() {
        long count = assessmentRepository.count();
        return String.format("ASS-%05d", count + 1);
    }
}
