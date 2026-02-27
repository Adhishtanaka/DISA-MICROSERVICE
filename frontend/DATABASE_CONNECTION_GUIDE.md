# 🔗 Backend-Database-Frontend Connection Setup

## Current Configuration Status

### ✅ Database (Neon PostgreSQL)
```
Host: ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech
Database: neondb
User: neondb_owner
Password: npg_wp8Te4WSLvGP
```

### ✅ Backend (Spring Boot Assessment Service)
```
Port: 8087
Database URL: Configured in application.yaml ✓
JPA/Hibernate: Configured for auto-updates ✓
```

### ✅ Frontend (React + TypeScript)
```
Port: 5173 (dev) / deployed domain (prod)
API Base URL: http://localhost:8087
```

---

## 🚀 Step 1: Verify Database Connection

### Test Database Connectivity

Open PowerShell and test the connection:

```powershell
# Install psql if needed
# choco install postgresql -y

# Test connection
psql 'postgresql://neondb_owner:npg_wp8Te4WSLvGP@ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'

# Once connected, verify tables
\dt
# Should show: public | assessment_table (or similar)

# Check data
SELECT * FROM assessment_table LIMIT 5;

# Exit
\q
```

---

## 🔧 Step 2: Start the Backend

### Build and Run Backend

```powershell
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service

# Clean build
mvn clean package -DskipTests

# Run the application
mvn spring-boot:run

# OR use the starter script if available
.\mvnw.cmd spring-boot:run
```

**Expected Output:**
```
Started AssessmentServiceApplication in X.XXX seconds
Tomcat started on port(s): 8087 (http)
```

**Verify Backend Running:**
```powershell
# In another PowerShell window
curl http://localhost:8087/api/assessments -Headers @{"Content-Type"="application/json"}
```

---

## 🎨 Step 3: Start the Frontend

### Build and Run Frontend

```powershell
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\frontend

# Ensure node_modules installed
npm install

# Start dev server
npm run dev
```

**Expected Output:**
```
VITE v7.3.1  ready in XXX ms

➜  Local:   http://localhost:5173/
```

---

## 🔌 Step 4: Verify Full Connection Stack

### Test Backend Health

**In Firefox/Chrome Console (while frontend is running):**

```javascript
// 1. Check API health
debugAPI()
// Should show: Backend health HEALTHY ✓

// 2. Test database connectivity
await assessmentAPI.getAllAssessments()
// Should return assessments from database

// 3. Check full stack
console.log({
  frontend: window.location.href,
  apiUrl: 'http://localhost:8087',
  database: 'neondb (Neon PostgreSQL)'
})
```

---

## 📊 Connection Diagram

```
┌─────────────────────┐
│    React Frontend   │
│   :5173 (dev)       │
└──────────┬──────────┘
           │ HTTP/API
           ↓
┌─────────────────────┐
│   Spring Boot       │
│ Assessment Service  │
│    :8087            │
└──────────┬──────────┘
           │ JDBC
           ↓
┌─────────────────────┐
│  Neon PostgreSQL    │
│   neondb            │
│  (AWS us-east-1)    │
└─────────────────────┘
```

---

## ✅ Troubleshooting Checklist

### Issue: Frontend can't connect to backend

```javascript
// In browser console, run:
debugAPI()

// If shows: useMockData: true = Backend not running
// If shows: isHealthy: false = Connection issue
```

**Solutions:**

1. **Backend not running?**
   ```powershell
   # Check if port 8087 is in use
   netstat -ano | findstr :8087
   
   # If nothing shown, start backend:
   mvn spring-boot:run
   ```

2. **Database connection failed?**
   ```powershell
   # Test psql connection
   psql 'postgresql://neondb_owner:npg_wp8Te4WSLvGP@ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'
   
   # Check table format matches Entity class
   \d+ assessment_table
   ```

3. **CORS blocked?**
   - Backend needs CORS configuration (see Step 5)

---

## 🔒 Step 5: Configure CORS on Backend

### Create CORS Configuration Class

Create file: `src/main/java/com/disa/config/CorsConfig.java`

```java
package com.disa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:5173",
                    "http://localhost:3000",
                    "http://localhost:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

**Then rebuild:**
```powershell
mvn clean package -DskipTests
mvn spring-boot:run
```

---

## 📋 Database Schema Verification

### Check Assessment Table Structure

```sql
-- Connect to database
psql 'postgresql://neondb_owner:npg_wp8Te4WSLvGP@ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'

-- List all tables
\dt

-- Check assessment table structure
\d+ assessment_table

-- Expected columns (sample):
-- id | assessment_code | incident_id | assessor_id | severity | findings | status | created_at | updated_at | ...
```

### If Table Doesn't Exist

When backend starts with `ddl-auto: update`, Hibernate will automatically create tables from entities.

**Force table creation:**
```yaml
# In application.yaml, change to:
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop  # Creates fresh schema on startup
      # OR use:
      ddl-auto: create       # Creates only if not exists
```

Then restart backend.

---

## 🧪 Testing Full Connection

### Manual API Testing

```powershell
# 1. Test backend is running
curl http://localhost:8087/api/assessments

# 2. Test database has data
curl http://localhost:8087/api/assessments/1

# 3. Create test data
curl -X POST http://localhost:8087/api/assessments `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{
    "assessmentCode": "TEST-001",
    "incidentId": 1,
    "assessorId": 123,
    "assessorName": "Test User",
    "severity": "HIGH",
    "findings": "Test findings",
    "recommendations": "Test recommendations"
  }'
```

### Browser Testing

**Open frontend:** `http://localhost:5173`

1. Check DevTools Console (F12)
   - Should NOT show CORS errors
   - Should show `[API]` logs
   - Should show assessments loading

2. Check Network tab
   - Look for GET request to `http://localhost:8087/api/assessments`
   - Response should be 200 with JSON array

3. Check UI
   - Dashboard should populate
   - Assessments should display
   - Operations should work

---

## 📊 Verify Data Sync

### Check if Frontend Gets Database Data

```javascript
// In browser console:
const response = await assessmentAPI.getAllAssessments()
console.log('Assessments from database:', response.data)

// Should show:
// [
//   {id: 1, assessmentCode: 'ASS-001', ...},
//   {id: 2, assessmentCode: 'ASS-002', ...}
// ]
```

### Check if Database Has Data

```powershell
psql 'postgresql://neondb_owner:npg_wp8Te4WSLvGP@ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'

-- Check data in table
SELECT * FROM assessment_table;

-- Check row count
SELECT COUNT(*) FROM assessment_table;

-- Exit
\q
```

---

## 🔄 Synchronization Issues

### If Data Is Not Updating

#### Issue 1: Hibernate Not Detecting Changes

**Solution:** Add `@DynamicUpdate` to Entity

```java
@Entity
@Table(name = "assessment_table")
@DynamicUpdate
@DynamicInsert
public class Assessment {
    // ... fields
}
```

#### Issue 2: Frontend Caching

**Solution:** Clear browser cache and restart

```javascript
// In console
localStorage.clear()
sessionStorage.clear()
location.reload()
```

#### Issue 3: Database Connection Lost

**Solution:** Check connection pool

```yaml
# In application.yaml, add:
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 600000
      max-lifetime: 1800000
```

#### Issue 4: Timeout Issues

**Solution:** Increase timeout

```javascript
// In frontend api.ts
axios.defaults.timeout = 60000 // 60 seconds instead of 30
```

---

## 🚀 Production Deployment Setup

### Environment Variables for Production

```bash
# Backend (application-prod.yaml)
server:
  port: 8087

spring:
  datasource:
    url: jdbc:postgresql://<PROD_HOST>/<PROD_DB>?sslmode=require&channel_binding=require
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    
  jpa:
    hibernate:
      ddl-auto: validate  # Don't auto-create in production

# Frontend (.env.production)
VITE_API_URL=https://api.yourdomain.com
```

### Docker Deployment

**Backend Dockerfile:**
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
COPY target/assessment-service-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

**Frontend Dockerfile:**
```dockerfile
FROM node:20-alpine AS builder
WORKDIR /app
COPY . .
RUN npm install && npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
```

---

## 📈 Monitoring Connection

### Backend Health Endpoint

```powershell
# Check backend health
curl http://localhost:8087/actuator/health

# Expected response:
# {"status":"UP"}
```

### Frontend Diagnostics

```javascript
// Comprehensive check
async function checkConnections() {
  console.log('🔍 Connection Check\n')
  
  // 1. Backend
  try {
    const health = await debugAPI()
    console.log('✅ Backend:', health.isHealthy ? 'HEALTHY' : 'DOWN')
  } catch (e) {
    console.log('❌ Backend: UNREACHABLE')
  }
  
  // 2. Database
  try {
    const data = await assessmentAPI.getAllAssessments()
    console.log('✅ Database:', `Connected (${data.data.length} records)`)
  } catch (e) {
    console.log('❌ Database: NO DATA')
  }
  
  // 3. Frontend
  console.log('✅ Frontend:', window.location.href)
}

checkConnections()
```

---

## ✅ Quick Start Commands

### Complete Setup (All at Once)

**Terminal 1: Start Database (already running on Neon)**
```powershell
# Already hosted, no action needed
# Connection verified below
```

**Terminal 2: Start Backend**
```powershell
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service
mvn spring-boot:run
```

**Terminal 3: Start Frontend**
```powershell
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\frontend
npm run dev
```

**Terminal 4: Test Connection**
```powershell
# Wait 10 seconds for services to start, then:
curl http://localhost:8087/api/assessments
```

---

## 🎯 Success Criteria

You'll know everything is connected when:

- ✅ Backend starts on port 8087 without errors
- ✅ Frontend starts on port 5173 without errors
- ✅ Browser shows "Backend health: HEALTHY" in debugAPI()
- ✅ Dashboard populates with data from database
- ✅ Create/Read/Update/Delete operations work
- ✅ Network tab shows 200 responses from API calls
- ✅ No CORS errors in console

---

## 📞 Debugging Commands

```javascript
// Frontend Console
debugAPI()                                  // Check backend health
await assessmentAPI.getAllAssessments()    // Test database access
enableDetailedLogging()                     // See API logs

// PowerShell
netstat -ano | findstr :8087                # Check backend port
netstat -ano | findstr :5173                # Check frontend port
curl http://localhost:8087/api/assessments # Direct backend test
curl http://localhost:5173                  # Frontend test
```

---

## 📚 Related Documentation

- [Backend Setup](../backend/assessment-service/00_START_HERE.md)
- [Frontend Setup](./SETUP.md)
- [API Testing](./API_TESTING.md)
- [Debugging](./DEBUGGING_GUIDE.md)

---

**Status:** Ready for connection ✅  
**Last Updated:** February 27, 2026
