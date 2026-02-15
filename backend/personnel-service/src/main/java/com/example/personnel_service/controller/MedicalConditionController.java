package com.example.personnel_service.controller;

import com.example.personnel_service.entity.MedicalCondition;
import com.example.personnel_service.service.MedicalConditionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/medical-conditions")
public class MedicalConditionController {
    private final MedicalConditionService medicalConditionService;

    public MedicalConditionController(MedicalConditionService medicalConditionService) {
        this.medicalConditionService = medicalConditionService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalCondition>> getAllMedicalConditions() {
        return ResponseEntity.ok(medicalConditionService.getAllMedicalConditions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalCondition> getMedicalConditionById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalConditionService.getMedicalConditionById(id));
    }

    @PostMapping
    public ResponseEntity<List<MedicalCondition>> createMedicalConditions(@RequestBody List<MedicalCondition> medicalConditions) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalConditionService.addMedicalCondition(medicalConditions));
    }

    @PutMapping
    public ResponseEntity<List<MedicalCondition>> updateMedicalConditions(@RequestBody List<MedicalCondition> medicalConditions) {
        return ResponseEntity.ok(medicalConditionService.updateMedicalCondition(medicalConditions));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MedicalCondition> softDeleteMedicalCondition(@PathVariable Long id) {
        return ResponseEntity.ok(medicalConditionService.softDeleteMedicalCondition(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalCondition(@PathVariable Long id) {
        medicalConditionService.deleteMedicalCondition(id);
        return ResponseEntity.noContent().build();
    }
}
