/*
 * TaskStatus enum representing the lifecycle state of a disaster response task.
 * Tasks transition from PENDING through IN_PROGRESS to COMPLETED.
 */
package com.disa.task_service.entity.enums;

public enum TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}
