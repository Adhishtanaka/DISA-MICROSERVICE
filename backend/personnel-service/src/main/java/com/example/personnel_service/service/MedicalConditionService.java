package com.example.personnel_service.service;

import com.example.personnel_service.entity.MedicalCondition;
import com.example.personnel_service.repository.MedicalConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalConditionService {
    private final MedicalConditionRepository repository;

    public MedicalConditionService(MedicalConditionRepository repository) {
        this.repository = repository;
    }

    public List<MedicalCondition> getAllMedicalConditions() {
        return repository.findAll();
    }

    public MedicalCondition getMedicalConditionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical condition not found"));
    }

    public List<MedicalCondition> addMedicalCondition(List<MedicalCondition> medicalConditions) {
        return repository.saveAll(medicalConditions);
    }

    public List<MedicalCondition> updateMedicalCondition(List<MedicalCondition> medicalConditions) {
        return repository.saveAll(medicalConditions);
    }

    public MedicalCondition softDeleteMedicalCondition(Long id) {
        MedicalCondition medicalCondition = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical condition not found"));
        medicalCondition.setDisabled(true);
        return repository.save(medicalCondition);
    }

    public void deleteMedicalCondition(Long id) {
        MedicalCondition medicalCondition = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical condition not found"));
        repository.delete(medicalCondition);
    }
}
