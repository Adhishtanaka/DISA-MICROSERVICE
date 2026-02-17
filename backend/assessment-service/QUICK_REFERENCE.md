
# Assessment Service - Quick Reference

## 🚀 Quick Start

### Build
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
cd "c:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service"
.\mvnw.cmd clean install -DskipTests
```

### Run
```powershell
java -jar target/assessment-service-0.0.1-SNAPSHOT.jar
```

### Access
```
http://localhost:8087/api/assessments
```

---

## 📋 API Examples

### 1️⃣ Create Assessment
```powershell
$body = @{
    incidentId = 1
    assessorId = 301
    assessorName = "John Assessor"
    severity = "CRITICAL"
    findings = "Severe structural damage"
    requiredActions = @("RESCUE", "MEDICAL_AID", "DEBRIS_REMOVAL")
    estimatedCasualties = 10
    estimatedDisplaced = 50
    status = "DRAFT"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8087/api/assessments" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body `
    -UseBasicParsing | Select-Object -ExpandProperty Content
```

### 2️⃣ Get All Assessments
```powershell
Invoke-WebRequest -Uri "http://localhost:8087/api/assessments" `
    -Method GET `
    -UseBasicParsing | Select-Object -ExpandProperty Content
```

### 3️⃣ Get Assessment by ID
```powershell
Invoke-WebRequest -Uri "http://localhost:8087/api/assessments/1" `
    -Method GET `
    -UseBasicParsing | Select-Object -ExpandProperty Content
```

### 4️⃣ Filter by Incident
```powershell
Invoke-WebRequest -Uri "http://localhost:8087/api/assessments/incident/1" `
    -Method GET `
    -UseBasicParsing | Select-Object -ExpandProperty Content
```

### 5️⃣ Update Assessment
```powershell
$updateBody = @{
    incidentId = 1
    assessorId = 301
    assessorName = "John Assessor - Updated"
    severity = "SEVERE"
    findings = "Updated findings"
    requiredActions = @("RESCUE")
    estimatedCasualties = 15
    estimatedDisplaced = 60
    status = "DRAFT"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8087/api/assessments/1" `
    -Method PUT `
    -ContentType "application/json" `
    -Body $updateBody `
    -UseBasicParsing | Select-Object -ExpandProperty Content
```

### 6️⃣ Complete Assessment (Triggers Event)
```powershell
Invoke-WebRequest -Uri "http://localhost:8087/api/assessments/1/complete" `
    -Method PUT `
    -UseBasicParsing | Select-Object -ExpandProperty Content
```

### 7️⃣ Get Completed Assessments
```powershell
Invoke-WebRequest -Uri "http://localhost:8087/api/assessments/status/completed" `
    -Method GET `
    -UseBasicParsing | Select-Object -ExpandProperty Content
```

### 8️⃣ Delete Assessment
```powershell
Invoke-WebRequest -Uri "http://localhost:8087/api/assessments/2" `
    -Method DELETE `
    -UseBasicParsing | Select-Object StatusCode
```

---

## 📁 File Structure

```
src/main/java/com/disa/assessment_service/
├── config/               → RabbitMQ Configuration
├── controller/           → REST Endpoints
├── dto/                  → Request/Response Objects
├── entity/               → JPA Entities & Enums
├── event/                → Event Models & Publisher
├── exception/            → Error Handling
├── repository/           → Data Access Layer
└── service/              → Business Logic
```

---

## 🗄️ Database

```
Connection: Neon PostgreSQL (Cloud)
Host: ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech
Database: neondb
Username: neondb_owner
Password: npg_wp8Te4WSLvGP
```

---

## 📊 Severity Levels

| Level | Description |
|-------|-------------|
| **MINOR** | Limited damage, minimal intervention |
| **MODERATE** | Significant damage, assistance needed |
| **SEVERE** | Extensive damage, urgent response |
| **CRITICAL** | Catastrophic, immediate response |

---

## ✅ Status Codes

| Status | Meaning |
|--------|---------|
| **DRAFT** | In progress |
| **COMPLETED** | Finalized & submitted |

---

## 🔄 Event Publishing

**Exchange:** `disaster.topic.exchange`  
**Queue:** `assessment.completed.queue`  
**Routing Key:** `assessment.completed`

Event published when assessment is completed with:
- Assessment ID & Code
- Incident ID
- Severity Level
- Required Actions
- Assessor Info
- Findings

---

## ⚙️ Configuration

**Port:** 8087  
**Upload Dir:** ./uploads  
**Max File Size:** 10MB  
**Hibernate DDL:** Update

---

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| Build Error (Java 21) | Edit pom.xml: `<java.version>17</java.version>` |
| JAVA_HOME not set | `$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"` |
| Port 8087 in use | Change `server.port` in application.yaml |
| DB Connection Error | Check network & credentials |
| RabbitMQ Error | Not critical - service can run without it |

---

## 📝 Assessment Fields

### Create/Update Request
```json
{
  "incidentId": number,
  "assessorId": number,
  "assessorName": "string",
  "severity": "CRITICAL|SEVERE|MODERATE|MINOR",
  "findings": "string (max 2000 chars)",
  "recommendations": "string (optional)",
  "requiredActions": ["RESCUE", "MEDICAL_AID", ...],
  "estimatedCasualties": number,
  "estimatedDisplaced": number,
  "affectedInfrastructure": "string (optional)",
  "status": "DRAFT|COMPLETED"
}
```

### Response Fields
- id (Auto-generated)
- assessmentCode (Auto-generated, e.g., ASS-00001)
- createdAt (Auto-generated)
- completedAt (Auto-generated when completed)
- photoUrls (Array of uploaded photo URLs)
- All input fields

---

## 🧪 Test Batch File

**Location:** `test-api.bat`  
**Usage:** Double-click or run from PowerShell

---

## 📦 Dependencies

| Dependency | Purpose |
|-----------|---------|
| Spring Boot | Framework |
| Spring Data JPA | Database ORM |
| Spring AMQP | Message Broker |
| PostgreSQL Driver | Database |
| Lombok | Code Generation |
| Jackson | JSON Processing |

---

## 🔐 Security Considerations

- ✅ Input validation on all endpoints
- ✅ File upload security (UUID-based names, path validation)
- ✅ SQL injection prevention (JPA parameterized queries)
- ✅ Exception handling (no stack traces in responses)
- ⚠️ TODO: Add Spring Security for authentication
- ⚠️ TODO: Add authorization checks

---

## 📈 Next Steps

1. Add authentication (Spring Security)
2. Implement pagination
3. Add search filters
4. Create unit tests
5. Add API documentation (Swagger)
6. Implement caching
7. Add monitoring/metrics
8. Containerize with Docker

---

**Service Version:** 1.0.0  
**Java Version:** 17 LTS  
**Spring Boot:** 3.2.1  
**Last Updated:** February 16, 2026

