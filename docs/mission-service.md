# Mission Service

**Port:** 8086
**Database:** PostgreSQL
**Purpose:** Manages disaster response missions — delivery, rescue, evacuation, and medical transport. Automatically creates missions in response to RabbitMQ events from other services.

---

## What It Does

Mission-service has two creation paths:
1. **Manual** — REST API calls to `POST /missions`
2. **Event-driven** — Automatically creates missions when it receives `incident.created`, `incident.escalated`, or `resource.critical_low` events via RabbitMQ

---

## API Routes

Base path: `/missions`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/missions` | Create a new mission manually |
| GET | `/missions` | Get all missions |
| GET | `/missions/{id}` | Get a mission by ID |
| GET | `/missions/status/{status}` | Filter missions by status |
| GET | `/missions/type/{type}` | Filter missions by type |
| PUT | `/missions/{id}/status` | Update mission status |
| DELETE | `/missions/{id}` | Delete a mission |

---

## Authentication & Security

- **Auth:** None — no Spring Security or JWT configured
- **CORS:** `@CrossOrigin(origins = "*")` on all endpoints (unrestricted)
- **Swagger/OpenAPI:** Available at `/swagger-ui` and `/api-docs`

---

## RabbitMQ

**Exchange:** `disaster.topic.exchange` (TopicExchange)

### Consumed Events

| Queue | Routing Key | Source Service | Action |
|-------|-------------|----------------|--------|
| `mission.incident.created.queue` | `incident.created` | incident-service | Create a `DELIVERY` mission for the incident |
| `mission.incident.escalated.queue` | `incident.escalated` | incident-service | Create a `RESCUE` mission for the escalated incident |
| `mission.resource.critical_low.queue` | `resource.critical_low` | resource-service | Create a `DELIVERY` mission for restocking |

### Event Payloads Consumed

**IncidentEvent** (`incident.created`):
```
eventType: String
timestamp: LocalDateTime
payload:
  incidentId:   Long
  incidentCode: String
  severity:     String
  address:      String
```

**IncidentEscalatedEvent** (`incident.escalated`):
```
eventType: String
payload:
  incidentId:   Long
  incidentCode: String
  newSeverity:  String
```

**ResourceEvent** (`resource.critical_low`):
```
eventType: String
payload:
  resourceId:   Long
  resourceCode: String
  resourceType: String
  location:     String
```

**No events published** by this service.

---

## DTOs

### MissionRequest
```
type:        MissionType (required)
origin:      String      (required)
destination: String      (required)
description: String      (optional)
cargoDetails: String     (optional)
vehicleType: String      (optional)
```

### StatusUpdateRequest
```
missionId: Long          (required)
status:    MissionStatus (required)
remarks:   String        (optional)
```

---

## Entities

### Mission
```
id:           Long         (PK, auto-generated)
missionCode:  String       (unique, e.g., MIS-001, MIS-002, ...)
type:         MissionType  (DELIVERY | RESCUE | EVACUATION | MEDICAL_TRANSPORT | ASSESMENT)
status:       MissionStatus (PENDING | IN_PROGRESS | COMPLETED | CANCELLED)
vehicleId:    String       (optional)
vehicleType:  String       (e.g., TRUCK, AMBULANCE)
driverId:     String       (optional)
driverName:   String       (optional)
origin:       String
destination:  String
description:  String       (max 1000 chars)
cargoDetails: String
incidentId:   Long         (ref to incident-service)
resourceId:   Long         (ref to resource-service)
createdAt:    LocalDateTime (auto, immutable)
startedAt:    LocalDateTime (set when status → IN_PROGRESS)
completedAt:  LocalDateTime (set when status → COMPLETED)
notes:        String
```

### MissionType Enum
`DELIVERY` | `RESCUE` | `EVACUATION` | `MEDICAL_TRANSPORT` | `ASSESMENT`

### MissionStatus Enum
`PENDING` | `IN_PROGRESS` | `COMPLETED` | `CANCELLED`

---

## Key Service Behaviors

- Mission codes auto-generated as `MIS-001`, `MIS-002`, ... (count-based)
- Auto-created DELIVERY missions use `"Central Warehouse"` as default origin
- `updateMissionStatus()` auto-records `startedAt` when → `IN_PROGRESS` and `completedAt` when → `COMPLETED`
- All mutations are `@Transactional`

### Repository Custom Queries
- `findByType(MissionType)`
- `findByStatus(MissionStatus)`
- `findByDriverId(String)`
- `findByVehicleId(String)`
- `findByOrigin(String)`
- `findByDestination(String)`
- `findByIncidentId(Long)`
- `findByResourceId(Long)`
- `findByTypeAndStatus(MissionType, MissionStatus)`

---

## Exception Handling

| Exception | HTTP Status |
|-----------|-------------|
| `RuntimeException` | 400 |
| `MethodArgumentNotValidException` | 400 (field-level) |
| `ServiceUnavailableException` | 503 |
| `DataAccessException` (DB down) | 503 |
| `AmqpConnectException` / `AmqpIOException` | 503 |
| `ConnectException` | 503 |
| Generic `Exception` | 500 |

All error responses include: `timestamp`, `status`, `error`, `message`, `service`.

---

## Configuration

| Property | Env Var | Default |
|----------|---------|---------|
| Server port | `SERVER_PORT` | `8086` |
| DB URL | `DB_URL` | (required) |
| DB Username | `DB_USERNAME` | (required) |
| DB Password | `DB_PASSWORD` | (required) |
| RabbitMQ Host | `RABBITMQ_HOST` | `localhost` |
| RabbitMQ Port | `RABBITMQ_PORT` | `5672` |
| RabbitMQ Username | `RABBITMQ_USERNAME` | (required) |
| RabbitMQ Password | `RABBITMQ_PASSWORD` | (required) |

Hibernate DDL: `update`
