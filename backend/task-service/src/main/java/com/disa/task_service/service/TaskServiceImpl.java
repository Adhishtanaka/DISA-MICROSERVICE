/*
 * TaskServiceImpl implementation for task service
 */
package com.disa.task_service.service;

import com.disa.task_service.dto.AssignTaskRequest;
import com.disa.task_service.dto.TaskRequest;
import com.disa.task_service.dto.TaskResponse;
import com.disa.task_service.entity.Task;
import com.disa.task_service.entity.TaskStatus;
import com.disa.task_service.event.AssessmentEvent;
import com.disa.task_service.event.EventPublisher;
import com.disa.task_service.event.TaskEvent;
import com.disa.task_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EventPublisher eventPublisher;

    @Override
    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setType(request.getType());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setIncidentId(request.getIncidentId());
        task.setLocation(request.getLocation());
        task.setStatus(TaskStatus.PENDING);
        task.setTaskCode(generateTaskCode());

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setType(request.getType());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setIncidentId(request.getIncidentId());
        task.setLocation(request.getLocation());

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    @Override
    public TaskResponse assignTask(Long id, AssignTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setAssignedTo(request.getAssignedTo());
        task.setStatus(TaskStatus.IN_PROGRESS);

        Task saved = taskRepository.save(task);

        // Publish event
        TaskEvent.TaskPayload payload = new TaskEvent.TaskPayload();
        payload.setTaskId(saved.getTaskCode());
        payload.setAssignedTo(saved.getAssignedTo().toString());
        payload.setTaskType(saved.getType().toString());
        payload.setPriority(saved.getPriority().toString());
        payload.setLocation(saved.getLocation());
        eventPublisher.publishTaskAssigned(payload);

        return mapToResponse(saved);
    }

    @Override
    public TaskResponse completeTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void createTasksFromAssessment(AssessmentEvent.AssessmentPayload payload) {
        // Simple implementation: create one task per required action
        for (String action : payload.getRequiredActions()) {
            TaskRequest request = new TaskRequest();
            // Map action to type, for simplicity
            if (action.contains("rescue")) {
                request.setType(com.disa.task_service.entity.TaskType.RESCUE_OPERATION);
            } else if (action.contains("medical")) {
                request.setType(com.disa.task_service.entity.TaskType.MEDICAL_AID);
            } else {
                request.setType(com.disa.task_service.entity.TaskType.DEBRIS_REMOVAL);
            }
            request.setTitle(action);
            request.setDescription("Auto-generated from assessment");
            request.setPriority(com.disa.task_service.entity.Priority.HIGH);
            request.setIncidentId(payload.getIncidentId());
            request.setLocation(payload.getLocation());

            createTask(request);
        }
    }

    private String generateTaskCode() {
        // Simple code generation
        return "TSK-" + System.currentTimeMillis();
    }

    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTaskCode(task.getTaskCode());
        response.setType(task.getType());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setIncidentId(task.getIncidentId());
        response.setAssignedTo(task.getAssignedTo());
        response.setLocation(task.getLocation());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setCompletedAt(task.getCompletedAt());
        return response;
    }
}