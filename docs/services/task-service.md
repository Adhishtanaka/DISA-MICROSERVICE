# Task Service — Deployment Guide

**Port:** 8088
**Base path:** `/api/v1`
**Database:** PostgreSQL (`task_db`)
**RabbitMQ:** Required (subscribes to `assessment.completed`)

---

## What it does

Manages granular response tasks (repair a road, set up comms, distribute supplies). Tasks can be assigned to responders by username. Subscribes to `assessment.completed` events to auto-generate tasks from assessment required-actions.

---

## Deploy on a separate server

### 1. Prerequisites

- A running **RabbitMQ** server — see [rabbitmq.md](../rabbitmq.md)

### 2. Start the service

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/backend/task-service

RABBITMQ_HOST=YOUR_RABBITMQ_IP docker compose up --build -d
```

### 3. Environment variables

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://postgres:5432/task_db` | PostgreSQL connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `RABBITMQ_HOST` | `rabbitmq` | **Set this to your RabbitMQ server IP/hostname** |
| `RABBITMQ_PORT` | `5672` | RabbitMQ AMQP port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |

---

## Verify it's running

```bash
docker compose logs task-service --tail=20

# All endpoints are under /api/v1
# Swagger UI
open http://YOUR_SERVER:8088/api/v1/swagger-ui.html
```

---

## Events consumed

| Queue | Routing Key | Action |
|---|---|---|
| `assessment.completed.queue` | `assessment.completed` | Auto-creates tasks from assessment required actions |
