package com.disa.assessment_service.controller;

import com.disa.assessment_service.dto.AssessmentRequest;
import com.disa.assessment_service.entity.Assessment;
import com.disa.assessment_service.service.AssessmentService;
import com.disa.assessment_service.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
@Tag(name = "Assessment", description = "Assessment management APIs")
public class AssessmentController {
    
    private final AssessmentService assessmentService;
    private final FileStorageService fileStorageService;
    
    @PostMapping
    @Operation(summary = "Create a new assessment")
    public ResponseEntity<Assessment> createAssessment(@Valid @RequestBody AssessmentRequest request) {
        Assessment assessment = assessmentService.createAssessment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assessment);
    }
    
    @GetMapping
    @Operation(summary = "Get all assessments")
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get assessment by ID")
    public ResponseEntity<Assessment> getAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }
    
    @GetMapping("/incident/{incidentId}")
    @Operation(summary = "Get assessments by incident ID")
    public ResponseEntity<List<Assessment>> getAssessmentsByIncident(@PathVariable Long incidentId) {
        return ResponseEntity.ok(assessmentService.getAssessmentsByIncident(incidentId));
    }
    
    @GetMapping("/completed")
    @Operation(summary = "Get all completed assessments")
    public ResponseEntity<List<Assessment>> getCompletedAssessments() {
        return ResponseEntity.ok(assessmentService.getCompletedAssessments());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update assessment")
    public ResponseEntity<Assessment> updateAssessment(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentRequest request) {
        Assessment assessment = assessmentService.updateAssessment(id, request);
        return ResponseEntity.ok(assessment);
    }
    
    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete assessment and publish event")
    public ResponseEntity<Assessment> completeAssessment(@PathVariable Long id) {
        Assessment assessment = assessmentService.completeAssessment(id);
        return ResponseEntity.ok(assessment);
    }
    
    @PostMapping("/{id}/photos")
    @Operation(summary = "Upload photo to assessment")
    public ResponseEntity<Assessment> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        Assessment assessment = assessmentService.uploadPhoto(id, file);
        return ResponseEntity.ok(assessment);
    }
    
    @GetMapping("/photos/{filename}")
    @Operation(summary = "Download photo")
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
    @Operation(summary = "Delete assessment")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }
}
