package com.example.personnel_service.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class InjuryHistoryDto {
    private long id;
    private String injuryType;
    private Date date;
    private String recoveryStatus;
    private String restrictions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;
}
