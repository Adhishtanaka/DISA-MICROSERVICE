# Assessment Service — Deployment Guide

**Port:** 8087
**Database:** PostgreSQL (`assessment_db`)
**RabbitMQ:** Required (publishes `assessment.completed` events)
**Storage:** Local volume for photo uploads (`/app/uploads`)

---

## What it does

Allows field responders to submit damage assessments with photo attachments. Optionally integrates with the Gemini AI API for damage classification. Publishes `assessment.completed` when an assessment is finalized.

---

## Deploy on a separate server

### 1. Prerequisites

- A running **RabbitMQ** server — see [rabbitmq.md](../rabbitmq.md)

### 2. Start the service

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/backend/assessment-service

RABBITMQ_HOST=YOUR_RABBITMQ_IP docker compose up --build -d
```

### 3. Environment variables

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://postgres:5432/assessment_db` | PostgreSQL connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `RABBITMQ_HOST` | `rabbitmq` | **Set this to your RabbitMQ server IP/hostname** |
| `RABBITMQ_PORT` | `5672` | RabbitMQ AMQP port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |

---

## File uploads

Photos are stored in `/app/uploads` inside the container. The docker-compose.yml mounts a named volume (`assessment_uploads`) to persist files across restarts.

For a separate-server deployment, mount a host directory:

```yaml
volumes:
  - /data/assessment-uploads:/app/uploads
```

---

## Verify it's running

```bash
docker compose logs assessment-service --tail=20

# Swagger UI
open http://YOUR_SERVER:8087/swagger-ui.html
```

---

## Events published

| Routing Key | Trigger | Consumed by |
|---|---|---|
| `assessment.completed` | Assessment marked complete | task-service |
