# Task Service

**Port:** 8088
**Context path:** `/api/v1`
**Database:** PostgreSQL
**Purpose:** Manages operational disaster response tasks — creation, assignment to personnel, and completion tracking. Auto-generates tasks from assessment completion events.

---

## What It Does

Task-service handles the lifecycle of individual work items within a disaster response operation. Tasks can be created manually via REST or auto-generated when an assessment is completed (via RabbitMQ). When a task is assigned, it publishes an event so that personnel-service can track who is on duty.

---

## API Routes

Base path: `/api/v1/tasks`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/tasks` | Create a new task (status: PENDING) |
| GET | `/api/v1/tasks` | Get all tasks |
| GET | `/api/v1/tasks/{id}` | Get task by ID |
| PUT | `/api/v1/tasks/{id}` | Update task fields (not status) |
| PUT | `/api/v1/tasks/{id}/assign` | Assign to personnel (status → IN_PROGRESS, publishes event) |
| PUT | `/api/v1/tasks/{id}/complete` | Mark as COMPLETED |
| DELETE | `/api/v1/tasks/{id}` | Delete a task |

---

## Authentication & Security

- **Auth:** None — CSRF disabled, all endpoints `permitAll()`
- **CORS origins:** `http://localhost:5173`, `http://localhost:3000`
- **Methods:** GET, POST, PUT, DELETE, OPTIONS, PATCH
- **Credentials:** allowed
- **Max age:** 3600 seconds

---

## RabbitMQ

**Exchange:** `disaster.topic.exchange` (TopicExchange)

### Consumed Events

| Queue | Routing Key | Source | Action |
|-------|-------------|--------|--------|
| `assessment.completed.queue` | `assessment.completed` | assessment-service | Auto-generate tasks from `requiredActions` |

### AssessmentEvent (consumed)
```
eventType: String
timestamp: LocalDateTime
payload:
  incidentId:      Long
  location:        String
  requiredActions: List<String>  (parsed to determine task type)
  severity:        String
```

**Auto-generation logic:**
- action contains `"rescue"` → `TaskType.RESCUE_OPERATION`
- action contains `"medical"` → `TaskType.MEDICAL_AID`
- otherwise → `TaskType.DEBRIS_REMOVAL`
- All auto-generated tasks: `Priority.HIGH`, `Status.PENDING`, linked to the incident

### Published Events

| Trigger | Routing Key | Queue | Payload |
|---------|-------------|-------|---------|
| `PUT /tasks/{id}/assign` | `task.assigned` | `task.assigned.queue` | `TaskEvent` |

### TaskEvent Structure
```
eventType: "task.assigned"
timestamp: LocalDateTime
payload:
  taskId:     String  (task code)
  assignedTo: String  (personnel ID as string)
  taskType:   String
  priority:   String
  location:   String
```

The `personnel-service` consumes `task.assigned` and can update personnel status to ON_DUTY.

---

## DTOs

### TaskRequest
```
type:        TaskType (required)
title:       String   (required)
description: String   (required)
priority:    Priority (required)
incidentId:  Long     (required)
location:    String   (required)
```

### TaskResponse
```
id:          Long
taskCode:    String         (unique, format: TSK-{timestamp})
type:        TaskType
title:       String
description: String
priority:    Priority
incidentId:  Long
assignedTo:  Long           (personnel ID, null until assigned)
location:    String
status:      TaskStatus
createdAt:   LocalDateTime
completedAt: LocalDateTime  (null until completed)
```

### AssignTaskRequest
```
assignedTo: Long (required — personnel ID)
```

---

## Entities

### Task
```
id:          Long       (PK, auto-generated)
taskCode:    String     (unique, TSK-{timestamp})
type:        TaskType   (RESCUE_OPERATION | MEDICAL_AID | DEBRIS_REMOVAL)
title:       String
description: String
priority:    Priority   (LOW | MEDIUM | HIGH | URGENT)
incidentId:  Long       (ref to incident-service)
assignedTo:  Long       (personnel ID)
location:    String
status:      TaskStatus (PENDING | IN_PROGRESS | COMPLETED)
createdAt:   LocalDateTime (auto-set on create)
completedAt: LocalDateTime (set when COMPLETED)
```

### TaskType Enum
`RESCUE_OPERATION` | `MEDICAL_AID` | `DEBRIS_REMOVAL`

### Priority Enum
`LOW` | `MEDIUM` | `HIGH` | `URGENT`

### TaskStatus Enum
`PENDING` | `IN_PROGRESS` | `COMPLETED`

---

## Key Service Behaviors

- Task codes auto-generated as `TSK-{timestamp}`
- `assign()` throws **409 Conflict** if task is already COMPLETED
- `complete()` throws **409 Conflict** if task is already COMPLETED
- `assign()` sets status to `IN_PROGRESS` and publishes `task.assigned` event
- `complete()` sets status to `COMPLETED` and records `completedAt`
- All mutations are `@Transactional`

---

## Exception Handling

| Exception | HTTP Status | Notes |
|-----------|-------------|-------|
| `TaskNotFoundException` | 404 | Task ID not found |
| `TaskAlreadyCompletedException` | 409 | Attempt to assign/complete a completed task |
| `InvalidTaskStateException` | 422 | Invalid state transition |
| `RuntimeException` | 400 | General runtime errors |
| Generic `Exception` | 500 | Unexpected errors |

All error responses include: `status`, `error`, `message`, `timestamp`.

---

## Configuration

| Property | Env Var | Default |
|----------|---------|---------|
| Server port | — | `8088` |
| DB URL | `DB_URL` | (required) |
| DB Username | `DB_USERNAME` | (required) |
| DB Password | `DB_PASSWORD` | (required) |
| RabbitMQ Host | `RABBITMQ_HOST` | `localhost` |
| RabbitMQ Port | `RABBITMQ_PORT` | `5672` |
| RabbitMQ Username | `RABBITMQ_USERNAME` | `admin` |
| RabbitMQ Password | `RABBITMQ_PASSWORD` | (required) |

Hibernate DDL: `update`
Java: 17
