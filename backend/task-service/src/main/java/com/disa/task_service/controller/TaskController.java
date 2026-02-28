/*
 * TaskController exposes the REST API for disaster response task management.
 * All endpoints are available under the /tasks path, prefixed by the global
 * /api/v1 context configured in application.yaml.
 */
package com.disa.task_service.controller;

import com.disa.task_service.dto.AssignTaskRequest;
import com.disa.task_service.dto.TaskRequest;
import com.disa.task_service.dto.TaskResponse;
import com.disa.task_service.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * POST /api/v1/tasks
     * Creates a new disaster response task.
     *
     * @param request the task creation payload
     * @return 201 Created with the new task body
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        log.info("POST /tasks - creating task: {}", request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    /**
     * GET /api/v1/tasks
     * Returns all tasks in the system.
     *
     * @return 200 OK with a list of all tasks
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        log.debug("GET /tasks - fetching all tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * GET /api/v1/tasks/{id}
     * Returns the task with the specified ID.
     *
     * @param id the task ID
     * @return 200 OK with the task, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        log.debug("GET /tasks/{} - fetching task", id);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * PUT /api/v1/tasks/{id}
     * Updates the fields of an existing task.
     *
     * @param id      the ID of the task to update
     * @param request the updated field values
     * @return 200 OK with the updated task, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        log.info("PUT /tasks/{} - updating task", id);
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    /**
     * PUT /api/v1/tasks/{id}/assign
     * Assigns the task to a personnel member and sets its status to IN_PROGRESS.
     *
     * @param id      the ID of the task to assign
     * @param request the assignment payload containing the personnel ID
     * @return 200 OK with the updated task, 404 if not found, or 409 if already completed
     */
    @PutMapping("/{id}/assign")
    public ResponseEntity<TaskResponse> assignTask(@PathVariable Long id, @RequestBody AssignTaskRequest request) {
        log.info("PUT /tasks/{}/assign - assigning to personnel {}", id, request.getAssignedTo());
        return ResponseEntity.ok(taskService.assignTask(id, request));
    }

    /**
     * PUT /api/v1/tasks/{id}/complete
     * Marks the task as COMPLETED and records the completion time.
     *
     * @param id the ID of the task to complete
     * @return 200 OK with the updated task, 404 if not found, or 409 if already completed
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id) {
        log.info("PUT /tasks/{}/complete - marking task complete", id);
        return ResponseEntity.ok(taskService.completeTask(id));
    }

    /**
     * DELETE /api/v1/tasks/{id}
     * Permanently deletes the task with the given ID.
     *
     * @param id the ID of the task to delete
     * @return 204 No Content on success, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("DELETE /tasks/{} - deleting task", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}