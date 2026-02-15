package com.example.personnel_service.controller;

import com.example.personnel_service.dto.PersonDto;
import com.example.personnel_service.dto.TaskAssignmentDto;
import com.example.personnel_service.dto.TaskDto;
import com.example.personnel_service.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    /**
     * Get all available persons
     * GET /api/personnel/assignments/available-persons
     */
    @GetMapping("/available-persons")
    public ResponseEntity<List<PersonDto>> getAvailablePersons() {
        return ResponseEntity.ok(assignmentService.getAvailablePersons());
    }

    /**
     * Get all pending tasks
     * GET /api/personnel/assignments/pending-tasks
     */
    @GetMapping("/pending-tasks")
    public ResponseEntity<List<TaskDto>> getPendingTasks() {
        return ResponseEntity.ok(assignmentService.getPendingTasks());
    }

    /**
     * Match a specific task to the most suitable person using Gemini AI
     * POST /api/personnel/assignments/match-task
     */
    @PostMapping("/match-task")
    public ResponseEntity<TaskAssignmentDto> matchTaskToPerson(@RequestBody TaskDto task) {
        try {
            TaskAssignmentDto assignment = assignmentService.matchTaskToPerson(task);
            return ResponseEntity.ok(assignment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Match all pending tasks to suitable persons using Gemini AI
     * POST /api/personnel/assignments/match-all-pending
     */
    @PostMapping("/match-all-pending")
    public ResponseEntity<List<TaskAssignmentDto>> matchAllPendingTasks() {
        try {
            List<TaskAssignmentDto> assignments = assignmentService.matchAllPendingTasks();
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
