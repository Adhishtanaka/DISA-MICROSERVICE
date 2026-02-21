package com.example.personnel_service.controller;

import com.example.personnel_service.dto.PersonDto;
import com.example.personnel_service.dto.TaskAssignmentDto;
import com.example.personnel_service.dto.TaskDto;
import com.example.personnel_service.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing task assignments to personnel.
 * Provides endpoints for matching tasks to suitable personnel using AI-powered algorithms.
 * Base path: /api/personnel/assignments
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    /**
     * Constructs a new AssignmentController with the specified AssignmentService.
     * 
     * @param assignmentService the service used to handle task assignment business logic
     */
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    /**
     * Retrieves all available persons who can be assigned to tasks.
     * GET /api/personnel/assignments/available-persons
     * 
     * @return ResponseEntity containing a list of available PersonDto objects
     */
    @GetMapping("/available-persons")
    public ResponseEntity<List<PersonDto>> getAvailablePersons() {
        return ResponseEntity.ok(assignmentService.getAvailablePersons());
    }

    /**
     * Retrieves all tasks that are pending assignment.
     * GET /api/personnel/assignments/pending-tasks
     * 
     * @return ResponseEntity containing a list of pending TaskDto objects
     */
    @GetMapping("/pending-tasks")
    public ResponseEntity<List<TaskDto>> getPendingTasks() {
        return ResponseEntity.ok(assignmentService.getPendingTasks());
    }

    /**
     * Matches a specific task to the most suitable person using Gemini AI.
     * The AI considers person skills, location, availability, and task requirements.
     * POST /api/personnel/assignments/match-task
     * 
     * @param task the TaskDto object to be matched with a suitable person
     * @return ResponseEntity containing the TaskAssignmentDto with matched person details
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
     * Matches all pending tasks to suitable persons using Gemini AI.
     * Processes all pending tasks and creates optimal assignments based on AI recommendations.
     * POST /api/personnel/assignments/match-all-pending
     * 
     * @return ResponseEntity containing a list of TaskAssignmentDto objects with matched assignments
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
