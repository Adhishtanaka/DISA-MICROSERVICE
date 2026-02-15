package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Skill;
import com.example.personnel_service.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @PostMapping
    public ResponseEntity<List<Skill>> createSkills(@RequestBody List<Skill> skills) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.addSkill(skills));
    }

    @PutMapping
    public ResponseEntity<List<Skill>> updateSkills(@RequestBody List<Skill> skills) {
        return ResponseEntity.ok(skillService.updateSkill(skills));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Skill> softDeleteSkill(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.softDeleteSkill(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
