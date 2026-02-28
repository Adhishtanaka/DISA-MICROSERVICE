package com.example.personnel_service.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class DocumentDto {
    private long id;
    private String name;
    private String url;
    private String note;
    private Date issueDate;
    private Date expDate;
    private String issuedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;
}
