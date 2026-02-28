package com.disa.assessment_service.event;

import com.disa.assessment_service.entity.Assessment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EventPublisher {
    
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;
    
    public void publishAssessmentCompleted(Assessment assessment) {
        if (rabbitTemplate == null) {
            log.warn("RabbitMQ not configured. Event not published for: {}", assessment.getAssessmentCode());
            return;
        }
        
        AssessmentEvent event = AssessmentEvent.builder()
            .eventType("assessment.completed")
            .timestamp(LocalDateTime.now())
            .payload(AssessmentPayload.builder()
                .assessmentId(assessment.getId())
                .assessmentCode(assessment.getAssessmentCode())
                .incidentId(assessment.getIncidentId())
                .severity(assessment.getSeverity().name())
                .requiredActions(assessment.getRequiredActions())
                .assessorId(assessment.getAssessorId())
                .findings(assessment.getFindings())
                .build())
            .build();
        
        log.info("Publishing assessment.completed event for: {}", assessment.getAssessmentCode());
        
        rabbitTemplate.convertAndSend(
            "disaster.topic.exchange",
            "assessment.completed",
            event
        );
    }
}
