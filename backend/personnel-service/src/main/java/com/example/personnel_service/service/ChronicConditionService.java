package com.example.personnel_service.service;

import com.example.personnel_service.entity.ChronicCondition;
import com.example.personnel_service.repository.ChronicConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChronicConditionService {
    private final ChronicConditionRepository repository;

    public ChronicConditionService(ChronicConditionRepository repository) {
        this.repository = repository;
    }
    public List<ChronicCondition> getAllChronicConditions() {
        return repository.findAll();
    }
    public ChronicCondition getChronicConditionById(long id) {
        return repository.findById(id).orElseThrow(()-> new RuntimeException("Chronic condition not found"));
    }
    public List<ChronicCondition> addChronicCondition(List<ChronicCondition> chronicCondition) {
        return repository.saveAll(chronicCondition);
    }
    public List<ChronicCondition> updateChronicCondition(List<ChronicCondition> chronicCondition) {
        return repository.saveAll(chronicCondition);
    }
    public ChronicCondition softDeleteChronicCondition(Long id) {
        ChronicCondition chronicCondition = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Chronic condition not found"));
        chronicCondition.setDisabled(true);
        return repository.save(chronicCondition);
    }
    public void deleteChronicCondition(long id) {
        ChronicCondition chronicCondition = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Chronic condition not found"));
        repository.delete(chronicCondition);
    }

}
