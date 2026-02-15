package com.example.personnel_service.service;

import com.example.personnel_service.entity.InjuryHistory;
import com.example.personnel_service.repository.InjuryHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InjuryService {
    private final InjuryHistoryRepository repository;

    public InjuryService(InjuryHistoryRepository repository) {
        this.repository = repository;
    }

    public List<InjuryHistory> getAllInjuryHistories() {
        return repository.findAll();
    }

    public InjuryHistory getInjuryHistoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Injury history not found"));
    }

    public List<InjuryHistory> addInjuryHistory(List<InjuryHistory> injuryHistories) {
        return repository.saveAll(injuryHistories);
    }

    public List<InjuryHistory> updateInjuryHistory(List<InjuryHistory> injuryHistories) {
        return repository.saveAll(injuryHistories);
    }

    public InjuryHistory softDeleteInjuryHistory(Long id) {
        InjuryHistory injuryHistory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Injury history not found"));
        injuryHistory.setDisabled(true);
        return repository.save(injuryHistory);
    }

    public void deleteInjuryHistory(Long id) {
        InjuryHistory injuryHistory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Injury history not found"));
        repository.delete(injuryHistory);
    }
}
