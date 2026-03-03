package com.disa.task_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelStatusEvent {

    private String eventType;
    private LocalDateTime timestamp;
    private PersonnelStatusPayload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonnelStatusPayload {
        private Long personnelId;
        private String personnelCode;
        private String fullName;
        private String status;
        private String role;
        private String skills;
        private Boolean isAvailable;
        private String notes;
    }
}
