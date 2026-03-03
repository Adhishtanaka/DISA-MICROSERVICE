package com.example.personnel_service.controller;

import com.example.personnel_service.dto.AssignmentHistoryDto;
import com.example.personnel_service.dto.PersonDto;
import com.example.personnel_service.dto.TaskAssignmentDto;
import com.example.personnel_service.dto.TaskDto;
import com.example.personnel_service.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> matchTaskToPerson(@RequestBody TaskDto task) {
        try {
            TaskAssignmentDto assignment = assignmentService.matchTaskToPerson(task);
            return ResponseEntity.ok(assignment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Assignment failed", "message", e.getMessage() != null ? e.getMessage() : "Unknown error"));
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
    public ResponseEntity<?> matchAllPendingTasks() {
        try {
            List<TaskAssignmentDto> assignments = assignmentService.matchAllPendingTasks();
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Batch assignment failed", "message", e.getMessage() != null ? e.getMessage() : "Unknown error"));
        }
    }

    @GetMapping("/history/{personId}")
    public ResponseEntity<List<AssignmentHistoryDto>> getAssignmentHistory(@PathVariable Long personId) {
        return ResponseEntity.ok(assignmentService.getAssignmentHistory(personId));
    }

    @GetMapping("/active/{personId}")
    public ResponseEntity<List<AssignmentHistoryDto>> getActiveAssignments(@PathVariable Long personId) {
        return ResponseEntity.ok(assignmentService.getActiveAssignments(personId));
    }

    @PutMapping("/{assignmentId}/complete")
    public ResponseEntity<?> completeAssignment(@PathVariable Long assignmentId) {
        try {
            AssignmentHistoryDto completed = assignmentService.completeAssignment(assignmentId);
            return ResponseEntity.ok(completed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Complete failed", "message", e.getMessage() != null ? e.getMessage() : "Unknown error"));
        }
    }
}
