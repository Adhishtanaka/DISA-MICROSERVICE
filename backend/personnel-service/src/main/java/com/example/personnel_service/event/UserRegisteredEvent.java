package com.example.personnel_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {

    private String eventType;
    private LocalDateTime timestamp;
    private UserPayload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPayload {
        private Long userId;
        private String username;
        private String email;
        private String fullName;
        private String phoneNumber;
        private String role;
    }
}
