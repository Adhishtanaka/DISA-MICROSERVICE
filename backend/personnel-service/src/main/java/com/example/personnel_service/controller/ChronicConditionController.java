package com.example.personnel_service.controller;

import com.example.personnel_service.entity.ChronicCondition;
import com.example.personnel_service.service.ChronicConditionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/chronic-conditions")
public class ChronicConditionController {
    private final ChronicConditionService chronicConditionService;

    public ChronicConditionController(ChronicConditionService chronicConditionService) {
        this.chronicConditionService = chronicConditionService;
    }

    @GetMapping
    public ResponseEntity<List<ChronicCondition>> getAllChronicConditions() {
        return ResponseEntity.ok(chronicConditionService.getAllChronicConditions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChronicCondition> getChronicConditionById(@PathVariable Long id) {
        return ResponseEntity.ok(chronicConditionService.getChronicConditionById(id));
    }

    @PostMapping
    public ResponseEntity<List<ChronicCondition>> createChronicConditions(@RequestBody List<ChronicCondition> chronicConditions) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chronicConditionService.addChronicCondition(chronicConditions));
    }

    @PutMapping
    public ResponseEntity<List<ChronicCondition>> updateChronicConditions(@RequestBody List<ChronicCondition> chronicConditions) {
        return ResponseEntity.ok(chronicConditionService.updateChronicCondition(chronicConditions));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChronicCondition> softDeleteChronicCondition(@PathVariable Long id) {
        return ResponseEntity.ok(chronicConditionService.softDeleteChronicCondition(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChronicCondition(@PathVariable Long id) {
        chronicConditionService.deleteChronicCondition(id);
        return ResponseEntity.noContent().build();
    }
}
