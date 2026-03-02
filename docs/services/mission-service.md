# Mission Service — Deployment Guide

**Port:** 8086
**Database:** PostgreSQL (`mission_db`)
**RabbitMQ:** Required (subscribes to incident and resource events)

---

## What it does

Manages response missions. Automatically creates mission stubs when incidents are reported (via RabbitMQ). Coordinators link missions to incidents and assign response teams.

---

## Deploy on a separate server

### 1. Prerequisites

- A running **RabbitMQ** server — see [rabbitmq.md](../rabbitmq.md)
- PostgreSQL (bundled or external)

### 2. Start the service

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/backend/mission-service

RABBITMQ_HOST=YOUR_RABBITMQ_IP docker compose up --build -d
```

### 3. Environment variables

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://postgres:5432/mission_db` | PostgreSQL connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `RABBITMQ_HOST` | `rabbitmq` | **Set this to your RabbitMQ server IP/hostname** |
| `RABBITMQ_PORT` | `5672` | RabbitMQ AMQP port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |
| `RABBITMQ_VIRTUAL_HOST` | `/` | RabbitMQ virtual host |

---

## Verify it's running

```bash
docker compose logs mission-service --tail=20

# Swagger UI
open http://YOUR_SERVER:8086/swagger-ui.html
# or
open http://YOUR_SERVER:8086/swagger-ui/index.html
```

---

## Events consumed

| Queue | Routing Key | Action |
|---|---|---|
| `mission.incident.created.queue` | `incident.created` | Auto-creates a mission stub |
| `mission.incident.escalated.queue` | `incident.escalated` | Updates mission priority |
| `mission.resource.critical_low.queue` | `resource.critical_low` | Alerts coordinator |
