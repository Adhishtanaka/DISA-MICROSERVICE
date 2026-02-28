package com.example.personnel_service.client;

import com.example.personnel_service.dto.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class TaskClient {

    private static final Logger log = LoggerFactory.getLogger(TaskClient.class);

    private final RestTemplate restTemplate;

    @Value("${external.task.api.url}")
    private String taskServiceUrl;

    public TaskClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<TaskDto> fetchTasks() {
        try {
            ResponseEntity<List<TaskDto>> response = restTemplate.exchange(
                    taskServiceUrl + "/tasks",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TaskDto>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch tasks from task-service: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
