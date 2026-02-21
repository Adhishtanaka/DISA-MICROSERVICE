package com.example.personnel_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a document associated with injury history.
 * Stores document metadata including URLs, issue and expiration dates.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String url;
    private String note;
    private LocalDateTime issueDate;
    private LocalDateTime expDate;
    private String issuedBy;


    @ManyToOne
    @JoinColumn(name = "injury_history_id")
    private InjuryHistory injuryHistory;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDisabled;

}
