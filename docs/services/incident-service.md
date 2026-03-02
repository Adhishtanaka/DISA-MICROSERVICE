# Incident Service — Deployment Guide

**Port:** 8083
**Database:** PostgreSQL (`incident_db`)
**RabbitMQ:** Required (publishes `incident.created`, `incident.escalated` events)

---

## What it does

Primary event source for the system. Manages disaster incidents and publishes events to RabbitMQ when incidents are created or escalated. Downstream services (mission, shelter) react automatically to these events.

---

## Deploy on a separate server

### 1. Prerequisites

- A running **RabbitMQ** server — see [rabbitmq.md](../rabbitmq.md)
- PostgreSQL (bundled in docker-compose or external)

### 2. Start the service

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/backend/incident-service

# Point to your shared RabbitMQ server
RABBITMQ_HOST=YOUR_RABBITMQ_IP docker compose up --build -d
```

### 3. Environment variables

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://postgres:5432/incident_db` | PostgreSQL connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `RABBITMQ_HOST` | `rabbitmq` | **Set this to your RabbitMQ server IP/hostname** |
| `RABBITMQ_PORT` | `5672` | RabbitMQ AMQP port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |

### 4. Connect to shared RabbitMQ

```bash
# Set RABBITMQ_HOST to the IP of your RabbitMQ server
RABBITMQ_HOST=192.168.1.10 docker compose up -d incident-service
```

---

## Verify it's running

```bash
docker compose logs incident-service --tail=20

# Test creating an incident (requires JWT token from auth-service)
curl -X POST http://YOUR_SERVER:8083/api/incidents \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Incident","description":"Test","severity":"HIGH","location":"Test City"}'

# Swagger UI
open http://YOUR_SERVER:8083/swagger-ui.html
```

---

## Events published

| Routing Key | Trigger | Consumed by |
|---|---|---|
| `incident.created` | New incident reported | mission-service, shelter-service |
| `incident.escalated` | Incident severity increased | mission-service |
