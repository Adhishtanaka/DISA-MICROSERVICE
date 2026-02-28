/*
 * TaskEvent is the message envelope published to RabbitMQ when a task changes state.
 * Contains event metadata (type, timestamp) and a TaskPayload with task details
 * consumed by downstream microservices such as notification or personnel services.
 */
package com.disa.task_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private TaskPayload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskPayload {
        private String taskId;
        private String assignedTo;
        private String taskType;
        private String priority;
        private String location;
    }
}