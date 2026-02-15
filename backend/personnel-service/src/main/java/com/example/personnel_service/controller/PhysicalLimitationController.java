package com.example.personnel_service.controller;

import com.example.personnel_service.entity.PhysicalLimitation;
import com.example.personnel_service.service.PhysicalLimitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/physical-limitations")
public class PhysicalLimitationController {
    private final PhysicalLimitationService physicalLimitationService;

    public PhysicalLimitationController(PhysicalLimitationService physicalLimitationService) {
        this.physicalLimitationService = physicalLimitationService;
    }

    @GetMapping
    public ResponseEntity<List<PhysicalLimitation>> getAllPhysicalLimitations() {
        return ResponseEntity.ok(physicalLimitationService.getAllPhysicalLimitations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhysicalLimitation> getPhysicalLimitationById(@PathVariable Long id) {
        return ResponseEntity.ok(physicalLimitationService.getPhysicalLimitationById(id));
    }

    @PostMapping
    public ResponseEntity<List<PhysicalLimitation>> createPhysicalLimitations(@RequestBody List<PhysicalLimitation> physicalLimitations) {
        return ResponseEntity.status(HttpStatus.CREATED).body(physicalLimitationService.addPhysicalLimitation(physicalLimitations));
    }

    @PutMapping
    public ResponseEntity<List<PhysicalLimitation>> updatePhysicalLimitations(@RequestBody List<PhysicalLimitation> physicalLimitations) {
        return ResponseEntity.ok(physicalLimitationService.updatePhysicalLimitation(physicalLimitations));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PhysicalLimitation> softDeletePhysicalLimitation(@PathVariable Long id) {
        return ResponseEntity.ok(physicalLimitationService.softDeletePhysicalLimitation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhysicalLimitation(@PathVariable Long id) {
        physicalLimitationService.deletePhysicalLimitation(id);
        return ResponseEntity.noContent().build();
    }
}
