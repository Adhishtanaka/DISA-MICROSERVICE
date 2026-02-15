package com.example.personnel_service.controller;

import com.example.personnel_service.entity.InjuryHistory;
import com.example.personnel_service.service.InjuryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/injury-histories")
public class InjuryHistoryController {
    private final InjuryService injuryService;

    public InjuryHistoryController(InjuryService injuryService) {
        this.injuryService = injuryService;
    }

    @GetMapping
    public ResponseEntity<List<InjuryHistory>> getAllInjuryHistories() {
        return ResponseEntity.ok(injuryService.getAllInjuryHistories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InjuryHistory> getInjuryHistoryById(@PathVariable Long id) {
        return ResponseEntity.ok(injuryService.getInjuryHistoryById(id));
    }

    @PostMapping
    public ResponseEntity<List<InjuryHistory>> createInjuryHistories(@RequestBody List<InjuryHistory> injuryHistories) {
        return ResponseEntity.status(HttpStatus.CREATED).body(injuryService.addInjuryHistory(injuryHistories));
    }

    @PutMapping
    public ResponseEntity<List<InjuryHistory>> updateInjuryHistories(@RequestBody List<InjuryHistory> injuryHistories) {
        return ResponseEntity.ok(injuryService.updateInjuryHistory(injuryHistories));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InjuryHistory> softDeleteInjuryHistory(@PathVariable Long id) {
        return ResponseEntity.ok(injuryService.softDeleteInjuryHistory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInjuryHistory(@PathVariable Long id) {
        injuryService.deleteInjuryHistory(id);
        return ResponseEntity.noContent().build();
    }
}
