/*
 * TaskService interface for task service
 */
package com.disa.task_service.service;

import com.disa.task_service.dto.AssignTaskRequest;
import com.disa.task_service.dto.TaskRequest;
import com.disa.task_service.dto.TaskResponse;
import com.disa.task_service.event.AssessmentEvent;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    List<TaskResponse> getAllTasks();
    TaskResponse getTaskById(Long id);
    TaskResponse updateTask(Long id, TaskRequest request);
    TaskResponse assignTask(Long id, AssignTaskRequest request);
    TaskResponse completeTask(Long id);
    void deleteTask(Long id);
    void createTasksFromAssessment(AssessmentEvent.AssessmentPayload payload);
}