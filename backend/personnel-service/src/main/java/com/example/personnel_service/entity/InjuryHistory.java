package com.example.personnel_service.entity;

import com.example.personnel_service.dto.DocumentDto;

import java.util.Date;
import java.util.List;

public class InjuryHistory {
    private long id;
    private String injuryType;
    private Date date;
    private String recoveryStatus;
    private String restrictions;
    private List<DocumentDto> pastDocuments;

}
