package com.example.personnel_service.service;

import com.example.personnel_service.entity.Skill;
import com.example.personnel_service.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {
    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    public List<Skill> getAllSkills() {
        return repository.findAll();
    }

    public Skill getSkillById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    public List<Skill> addSkill(List<Skill> skills) {
        return repository.saveAll(skills);
    }

    public List<Skill> updateSkill(List<Skill> skills) {
        return repository.saveAll(skills);
    }

    public Skill softDeleteSkill(Long id) {
        Skill skill = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        skill.setDisabled(true);
        return repository.save(skill);
    }

    public void deleteSkill(Long id) {
        Skill skill = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        repository.delete(skill);
    }
}
