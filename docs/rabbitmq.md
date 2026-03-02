# RabbitMQ — Shared Message Broker

All backend services that use events connect to **one shared RabbitMQ instance**. Deploy this first before deploying any backend service that needs it.

---

## Deploy RabbitMQ

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/docker

docker compose up -d
```

RabbitMQ starts on:
- `5672` — AMQP (application connections)
- `15672` — Management UI → `http://YOUR_SERVER:15672` (guest/guest)

---

## Connect backend services to this RabbitMQ

Each backend service reads `RABBITMQ_HOST` from its environment. Set it to the IP or hostname of this server:

```bash
# When starting any backend service:
RABBITMQ_HOST=192.168.1.10 docker compose up -d
```

Or set in each service's `docker-compose.yml`:

```yaml
environment:
  RABBITMQ_HOST: 192.168.1.10   # IP of your RabbitMQ server
  RABBITMQ_PORT: 5672
  RABBITMQ_USERNAME: guest
  RABBITMQ_PASSWORD: guest
```

---

## Services that require RabbitMQ

| Service | Role |
|---|---|
| incident-service | Publishes `incident.created`, `incident.escalated` |
| mission-service | Subscribes to incident and resource events |
| resource-service | Publishes `resource.critical_low` |
| shelter-service | Subscribes to `incident.created` |
| assessment-service | Publishes `assessment.completed` |
| task-service | Subscribes to `assessment.completed` |
| personnel-service | Subscribes to task events |

**auth-service** does not use RabbitMQ.

---

## Exchange and queue layout

All events use the `disaster.topic.exchange` (type: `topic`).

| Routing Key | Published by | Subscribed queues |
|---|---|---|
| `incident.created` | incident-service | `mission.incident.created.queue`, `shelter.incident.created.queue` |
| `incident.escalated` | incident-service | `mission.incident.escalated.queue` |
| `resource.critical_low` | resource-service | `mission.resource.critical_low.queue` |
| `assessment.completed` | assessment-service | `assessment.completed.queue` |

Queues are auto-created by each service on startup.
