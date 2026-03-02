# Incident Service

**Port:** 8083
**Database:** PostgreSQL (configured via `DB_URL` env var)
**Purpose:** Core service for reporting, tracking, and escalating disaster incidents. Publishes events that drive the rest of the system.

---

## What It Does

The incident-service is the entry point for disaster data. When a new incident is created or escalated, it publishes RabbitMQ events that trigger automatic responses in the mission-service, shelter-service, and task-service.

---

## API Routes

Base path: `/api/incidents`
All endpoints support `@CrossOrigin(origins = "*")`.

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/incidents` | Create a new incident (publishes `incident.created` event) |
| GET | `/api/incidents` | Get all incidents (paginated, params: `page=0`, `size=10`) |
| GET | `/api/incidents/{id}` | Get incident by ID |
| PUT | `/api/incidents/{id}` | Update incident fields (no event published) |
| PUT | `/api/incidents/{id}/escalate` | Escalate severity (publishes `incident.escalated` event) |
| PUT | `/api/incidents/{id}/status` | Update status only — no event published |
| DELETE | `/api/incidents/{id}` | Delete incident |
| GET | `/api/incidents/status/{status}` | Get incidents filtered by status |

---

## Authentication & Security

- **Auth:** None — Spring Security is a dependency but no security config is implemented. All endpoints are publicly accessible.
- **CORS:** `@CrossOrigin(origins = "*")` on all controller methods (unrestricted)

---

## RabbitMQ

**Exchange:** `disaster.topic.exchange` (TopicExchange)

### Published Events

| Trigger | Routing Key | Payload |
|---------|-------------|---------|
| `POST /api/incidents` | `incident.created` | `IncidentEvent` with `IncidentPayload` |
| `PUT /api/incidents/{id}/escalate` | `incident.escalated` | `IncidentEscalatedEvent` with `EscalationPayload` |

### IncidentEvent (incident.created)
```
eventType: "incident.created"
timestamp: LocalDateTime
payload:
  incidentId:   Long
  incidentCode: String   (e.g., "INC-001")
  type:         String   (IncidentType name)
  severity:     String   (Severity name)
  latitude:     Double
  longitude:    Double
  address:      String
  description:  String
```

### IncidentEscalatedEvent (incident.escalated)
```
eventType: "incident.escalated"
timestamp: LocalDateTime
payload:
  incidentId:       Long
  incidentCode:     String
  previousSeverity: String
  newSeverity:      String
  reason:           String  ("Manual escalation")
```

**Consumers of these events:**
- `mission-service` — creates DELIVERY mission on `incident.created`, RESCUE mission on `incident.escalated`
- `shelter-service` — prepares nearby shelters on `incident.created`

**No events consumed** by this service.

---

## DTOs

### IncidentRequest
```
type:        IncidentType (required)
severity:    Severity     (required)
description: String       (required)
latitude:    Double       (required)
longitude:   Double       (required)
address:     String       (required)
```

### IncidentResponse
```
id:           Long
incidentCode: String
type:         IncidentType
severity:     Severity
status:       IncidentStatus
description:  String
latitude:     Double
longitude:    Double
address:      String
reportedAt:   LocalDateTime
updatedAt:    LocalDateTime
```

### EscalateRequest
```
newSeverity: Severity (required)
reason:      String   (optional)
```

---

## Entities

### Incident
```
id:           Long          (PK, auto-generated via IDENTITY)
incidentCode: String        (unique, e.g., INC-001, INC-002, ...)
type:         IncidentType  (stored as STRING)
severity:     Severity      (stored as STRING)
status:       IncidentStatus (stored as STRING, default: REPORTED)
description:  String
latitude:     Double
longitude:    Double
address:      String
reportedAt:   LocalDateTime (auto via @CreationTimestamp)
updatedAt:    LocalDateTime (auto via @UpdateTimestamp)
```

### IncidentType Enum
`EARTHQUAKE` | `FLOOD` | `FIRE` | `LANDSLIDE` | `TSUNAMI` | `CYCLONE` | `DROUGHT`

### Severity Enum
`LOW` | `MEDIUM` | `HIGH` | `CRITICAL`

### IncidentStatus Enum
`REPORTED` (initial) | `ACTIVE` | `RESOLVED`

---

## Key Service Behaviors

- Incident codes are auto-generated as `INC-001`, `INC-002`, ... (count-based)
- New incidents always start with status `REPORTED`
- `createIncident()` and `escalateIncident()` trigger RabbitMQ events; `updateIncident()` and `updateIncidentStatus()` do not
- `escalateIncident()` captures the previous severity before updating for the event payload
- `getAllIncidents()` supports pagination via Spring Data `Pageable`

### Repository Custom Queries
- `findByStatus(IncidentStatus)` — filter by status
- `findByType(IncidentType)` — filter by type
- `findBySeverity(Severity)` — filter by severity
- `findByLocationBounds(minLat, maxLat, minLng, maxLng)` — geographic bounding box query
- `countByStatus(IncidentStatus)` — aggregate count

---

## Exception Handling

| Exception | HTTP Status |
|-----------|-------------|
| `RuntimeException` (not found) | 404 |
| `MethodArgumentNotValidException` | 400 (field-level) |
| Generic `Exception` | 500 |

All error responses include: `timestamp`, `status`, `error`, `message`.

---

## Configuration

| Property | Env Var | Default |
|----------|---------|---------|
| Server port | — | `8083` |
| DB URL | `DB_URL` | (required) |
| DB Username | `DB_USERNAME` | (required) |
| DB Password | `DB_PASSWORD` | (required) |
| RabbitMQ Host | `RABBITMQ_HOST` | `localhost` |
| RabbitMQ Port | `RABBITMQ_PORT` | `5672` |
| RabbitMQ Username | `RABBITMQ_USERNAME` | `admin` |
| RabbitMQ Password | `RABBITMQ_PASSWORD` | (required) |

Hibernate DDL: `update`
