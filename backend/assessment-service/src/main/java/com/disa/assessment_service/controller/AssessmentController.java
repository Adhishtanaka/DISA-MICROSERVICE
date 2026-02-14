package com.disa.assessment_service.controller;

import com.disa.assessment_service.dto.AssessmentRequest;
import com.disa.assessment_service.entity.Assessment;
import com.disa.assessment_service.service.AssessmentService;
import com.disa.assessment_service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {
    
    private final AssessmentService assessmentService;
    private final FileStorageService fileStorageService;
    
    @PostMapping
    public ResponseEntity<Assessment> createAssessment(@Valid @RequestBody AssessmentRequest request) {
        Assessment assessment = assessmentService.createAssessment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assessment);
    }
    
    @GetMapping
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Assessment> getAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }
    
    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<List<Assessment>> getAssessmentsByIncident(@PathVariable Long incidentId) {
        return ResponseEntity.ok(assessmentService.getAssessmentsByIncident(incidentId));
    }
    
    @GetMapping("/assessor/{assessorId}")
    public ResponseEntity<List<Assessment>> getAssessmentsByAssessor(@PathVariable Long assessorId) {
        return ResponseEntity.ok(assessmentService.getAssessmentsByAssessor(assessorId));
    }
    
    @GetMapping("/status/completed")
    public ResponseEntity<List<Assessment>> getCompletedAssessments() {
        return ResponseEntity.ok(assessmentService.getCompletedAssessments());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Assessment> updateAssessment(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentRequest request) {
        Assessment assessment = assessmentService.updateAssessment(id, request);
        return ResponseEntity.ok(assessment);
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<Assessment> completeAssessment(@PathVariable Long id) {
        Assessment assessment = assessmentService.completeAssessment(id);
        return ResponseEntity.ok(assessment);
    }
    
    @PostMapping("/{id}/photos")
    public ResponseEntity<Assessment> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        Assessment assessment = assessmentService.uploadPhoto(id, file);
        return ResponseEntity.ok(assessment);
    }
    
    @GetMapping("/photos/{filename}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.loadFile(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
                
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }
}
