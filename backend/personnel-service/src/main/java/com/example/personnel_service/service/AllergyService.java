package com.example.personnel_service.service;

import com.example.personnel_service.entity.Allergy;
import com.example.personnel_service.repository.AllergyRepository;

import java.util.List;

public class AllergyService {
    private AllergyRepository _allergyRepository;
    public List<Allergy> addAllergy(List<Allergy> allergy) {
        return _allergyRepository.saveAll(allergy);
    }

    public List<Allergy> updateAllergy(List<Allergy> allergy) {
        return _allergyRepository.saveAll(allergy);
    }
    public Allergy deleteAllergy(Long id) {
        return _allergyRepository.save(Allergy);
    }
}
