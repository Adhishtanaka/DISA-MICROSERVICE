# Resource Service

**Port:** 8089
**Database:** PostgreSQL (`resource_db`)
**Purpose:** Manages disaster relief resource inventory (food, water, medicine, equipment, etc.) with stock tracking and low-stock alerting via RabbitMQ.

---

## What It Does

Resource-service provides full inventory management for disaster relief supplies. It tracks stock levels, supports granular stock operations (set/increment/decrement), and automatically publishes a `resource.critical_low` event whenever stock falls below a configured threshold. The mission-service consumes this event to dispatch restocking missions.

---

## API Routes

Base path: `/api/resources`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/resources` | Create a new resource |
| GET | `/api/resources` | Get all resources |
| GET | `/api/resources/{id}` | Get resource by ID |
| GET | `/api/resources/type/{type}` | Filter resources by type |
| GET | `/api/resources/low-stock` | Get all resources below their threshold |
| PUT | `/api/resources/{id}` | Full update of a resource |
| PUT | `/api/resources/{id}/stock` | Set stock to a specific quantity |
| PUT | `/api/resources/{id}/increment` | Increase stock by a quantity |
| PUT | `/api/resources/{id}/decrement` | Decrease stock by a quantity |
| DELETE | `/api/resources/{id}` | Delete a resource |

---

## Authentication & Security

- **Auth:** Spring Security configured but **permissive** — all endpoints allow anonymous access (`permitAll()`)
- **CSRF:** Disabled
- **Note:** Development configuration only; production should enforce JWT

---

## RabbitMQ

**Exchange:** `disaster.topic.exchange` (TopicExchange)

### Published Events

| Trigger | Routing Key | Queue | Condition |
|---------|-------------|-------|-----------|
| Any stock update | `resource.critical_low` | `resource.critical_low.queue` | Only if `currentStock < threshold` |

Triggered by: `updateStock()`, `incrementStock()`, `decrementStock()`, `updateResource()`

### ResourceEvent Structure
```
eventType: "resource.critical_low"
timestamp: LocalDateTime
payload:
  resourceId:   Long
  resourceCode: String
  name:         String
  currentStock: Integer
  threshold:    Integer
  location:     String
```

The `mission-service` consumes `resource.critical_low` and creates a DELIVERY mission for restocking.

**No events consumed** by this service.

---

## DTOs

### ResourceRequest
```
resourceCode:  String       (required, e.g., "RES-101")
type:          ResourceType (required)
name:          String       (required)
description:   String       (optional)
currentStock:  Integer      (required, positive)
threshold:     Integer      (required, positive)
unit:          String       (required, e.g., "kg", "liters", "pieces")
location:      String       (required)
```

### ResourceResponse
```
id:           Long
resourceCode: String
type:         ResourceType
name:         String
description:  String
currentStock: Integer
threshold:    Integer
unit:         String
location:     String
createdAt:    LocalDateTime
updatedAt:    LocalDateTime
```

### StockUpdateRequest
```
quantity: Integer (required, positive)
```
Used by `/stock`, `/increment`, `/decrement` endpoints.

---

## Entities

### Resource
```
id:           Long         (PK, auto-generated)
resourceCode: String       (unique)
type:         ResourceType (stored as STRING)
name:         String
description:  String
currentStock: Integer      (current inventory level)
threshold:    Integer      (low-stock alert threshold)
unit:         String       (measurement unit)
location:     String       (warehouse/storage location)
createdAt:    LocalDateTime (auto via @PrePersist)
updatedAt:    LocalDateTime (auto via @PreUpdate)
```

### ResourceType Enum
| Value | Examples |
|-------|---------|
| `FOOD` | Rice, canned food |
| `WATER` | Bottled water, water tanks |
| `MEDICINE` | First aid, medications |
| `EQUIPMENT` | Tents, blankets, tools |
| `CLOTHING` | Clothes, shoes |
| `HYGIENE` | Soap, sanitizers |

---

## Key Service Behaviors

- `decrementStock()` throws `IllegalArgumentException` if requested decrement exceeds current stock
- Low-stock event is **only published** when `currentStock < threshold` after any write operation
- All mutations are `@Transactional`
- Reads are `@Transactional(readOnly = true)`

---

## Exception Handling

| Exception | HTTP Status |
|-----------|-------------|
| `ResourceNotFoundException` | 404 |
| `IllegalArgumentException` (insufficient stock) | 400 |
| Generic `Exception` | 500 |

---

## Configuration

| Property | Env Var | Default |
|----------|---------|---------|
| Server port | — | `8089` |
| DB URL | `DB_URL` | `jdbc:postgresql://localhost:5432/resource_db` |
| DB Username | `DB_USERNAME` | `postgres` |
| DB Password | `DB_PASSWORD` | `postgres` |
| RabbitMQ Host | `RABBITMQ_HOST` | `localhost` |
| RabbitMQ Port | `RABBITMQ_PORT` | `5672` |
| RabbitMQ Username | `RABBITMQ_USERNAME` | `guest` |
| RabbitMQ Password | `RABBITMQ_PASSWORD` | `guest` |

Hibernate DDL: `update`
Swagger UI: `/swagger-ui` (title: "Resource Service API", version: 1.0.0)
