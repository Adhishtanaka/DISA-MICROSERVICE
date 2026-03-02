# Resource Service — Deployment Guide

**Port:** 8089
**Database:** PostgreSQL (`resource_db`)
**RabbitMQ:** Required (publishes `resource.critical_low` events)

---

## What it does

Tracks physical disaster response resources (vehicles, medical kits, generators, etc.). Publishes a `resource.critical_low` event to RabbitMQ when stock falls below a threshold.

---

## Deploy on a separate server

### 1. Prerequisites

- A running **RabbitMQ** server — see [rabbitmq.md](../rabbitmq.md)

### 2. Start the service

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/backend/resource-service

RABBITMQ_HOST=YOUR_RABBITMQ_IP docker compose up --build -d
```

### 3. Environment variables

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://postgres:5432/resource_db` | PostgreSQL connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `RABBITMQ_HOST` | `rabbitmq` | **Set this to your RabbitMQ server IP/hostname** |
| `RABBITMQ_PORT` | `5672` | RabbitMQ AMQP port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |

---

## Verify it's running

```bash
docker compose logs resource-service --tail=20

# Swagger UI
open http://YOUR_SERVER:8089/swagger-ui.html
```

---

## Events published

| Routing Key | Trigger | Consumed by |
|---|---|---|
| `resource.critical_low` | Stock quantity falls below threshold | mission-service |
