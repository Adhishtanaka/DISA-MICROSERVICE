/*
 * TaskEvent for task service
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