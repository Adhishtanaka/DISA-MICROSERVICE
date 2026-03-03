package com.example.personnel_service.client;

import com.example.personnel_service.dto.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class TaskClient {

    private static final Logger log = LoggerFactory.getLogger(TaskClient.class);

    private final RestTemplate restTemplate;

    @Value("${external.task.api.url}")
    private String taskServiceUrl;

    public TaskClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(java.time.Duration.ofSeconds(10));
        factory.setReadTimeout(java.time.Duration.ofSeconds(30));
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * Gets the JWT Authorization header from the current HTTP request context
     * so it can be forwarded to task-service for authentication.
     */
    private String getCurrentAuthToken() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            return attrs.getRequest().getHeader("Authorization");
        }
        return null;
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String authToken = getCurrentAuthToken();
        if (authToken != null) {
            headers.set("Authorization", authToken);
        }
        return headers;
    }

    public List<TaskDto> fetchTasks() {
        try {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<List<TaskDto>> response = restTemplate.exchange(
                    taskServiceUrl + "/tasks",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<TaskDto>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch tasks from task-service: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean assignTask(Long taskId, Long personnelId) {
        try {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Map<String, Long>> entity = new HttpEntity<>(
                    Map.of("assignedTo", personnelId), headers);
            restTemplate.exchange(
                    taskServiceUrl + "/tasks/" + taskId + "/assign",
                    HttpMethod.PUT,
                    entity,
                    String.class);
            log.info("Successfully assigned task {} to personnel {}", taskId, personnelId);
            return true;
        } catch (Exception e) {
            log.error("Failed to assign task {} to personnel {}: {}", taskId, personnelId, e.getMessage());
            return false;
        }
    }
}
