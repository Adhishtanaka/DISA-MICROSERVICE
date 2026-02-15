package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Medication;
import com.example.personnel_service.service.MedicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/medications")
public class MedicationController {
    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @PostMapping
    public ResponseEntity<List<Medication>> createMedications(@RequestBody List<Medication> medications) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicationService.addMedication(medications));
    }

    @PutMapping
    public ResponseEntity<List<Medication>> updateMedications(@RequestBody List<Medication> medications) {
        return ResponseEntity.ok(medicationService.updateMedication(medications));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Medication> softDeleteMedication(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.softDeleteMedication(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
