# 🔧 Complete Backend-Database-Frontend Integration Troubleshooting

## 🎯 Quick Diagnostics (Run First)

### Step 1: Is Backend Running?

```powershell
# Terminal 1: Check for Java process
Get-Process | Where-Object { $_.Name -eq "java" }

# Terminal 2: Or try connecting
curl http://localhost:8087/api/assessments

# Terminal 3: Or check port
netstat -ano | findstr :8087
```

**✅ Yes (Port 8087 listening)** → Go to Step 2  
**❌ No (Nothing shown)** → Go to **"Backend Not Starting"** section

---

### Step 2: Can Frontend Reach Backend?

```javascript
// In browser console (frontend must be running on :5173)
debugAPI()

// Look for: "Backend: HEALTHY" or "Using mock data"
// If "Using mock data" = Backend not responding
```

**✅ HEALTHY** → Go to Step 3  
**❌ Using mock data** → Go to **"Frontend Can't Reach Backend"** section

---

### Step 3: Does Database Have Data?

```javascript
// In browser console
await assessmentAPI.getAllAssessments()

// Should return array (even if empty [])
```

**✅ Shows data** → ✅ All Connected!  
**❌ Empty array** → Go to **"Database Empty"** section

---

## ❌ Problem Solutions

### Problem 1: Backend Not Starting

**Symptoms:**
```
Process "java" not found
OR
Connection refused on :8087
OR
Failed to start spring boot
```

**Solution A: Missing Prerequisites**

```powershell
# Check Java installation
java -version
# Should show: openjdk 17.x.x (or similar)

# If not installed:
choco install openjdk17 -y
# Restart PowerShell after install

java -version  # Verify
```

**Solution B: Missing Maven**

```powershell
# Check Maven installation
mvn -version
# Should show: Apache Maven 3.8.x

# If not installed:
choco install maven -y
# Restart PowerShell after install

mvn -version  # Verify
```

**Solution C: Build Errors**

```powershell
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service

# Clean and rebuild
mvn clean install -DskipTests

# Check for errors in output
# Look for: [ERROR] or build failure

# If errors found:
mvn -X clean install -DskipTests  # Verbose output
```

**Solution D: Start Backend Correctly**

```powershell
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service

# Method 1 (Recommended)
mvn spring-boot:run

# Method 2
.\mvnw.cmd spring-boot:run

# Method 3
java -jar target/assessment-service-0.0.1-SNAPSHOT.jar

# Wait 10-15 seconds for startup
# Should see: "Started AssessmentServiceApplication in X.XXX seconds"
```

**Solution E: Port Already in Use**

```powershell
# Find what's using port 8087
netstat -ano | findstr :8087

# Kill the process (replace PID)
taskkill /PID <PID> /F

# Change port in application.yaml if needed:
# server:
#   port: 8088  (change to different port)
```

---

### Problem 2: Database Connection Failed

**Symptoms:**
```
SQL State [08001]: Cannot get a connection
Connection timeout
PSQLException: Connection refused
```

**Diagnostics:**

```powershell
# 1. Test connection with psql
psql 'postgresql://neondb_owner:npg_wp8Te4WSLvGP@ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'

# 2. If fails, check internet
ping google.com

# 3. Check credentials
# Username: neondb_owner ✓
# Password: npg_wp8Te4WSLvGP ✓
# Host: ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech ✓
# Database: neondb ✓
```

**Solution A: Network/Firewall Issues**

```powershell
# Allow Java through Windows Firewall
New-NetFirewallRule `
  -DisplayName "Java Application" `
  -Direction Inbound `
  -Program "C:\Program Files\Java\jdk-17*\bin\java.exe" `
  -Action Allow

# Or disable firewall for testing (not recommended for production)
Set-NetFirewallProfile -Profile Domain,Public,Private -Enabled $false
```

**Solution B: Database Credentials Wrong**

```powershell
# Verify in application.yaml:
# src/main/resources/application.yaml

# Should be:
# spring:
#   datasource:
#     url: jdbc:postgresql://ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require
#     username: neondb_owner
#     password: npg_wp8Te4WSLvGP

# Fix if needed, then restart backend
```

**Solution C: SSLMode Issue**

```powershell
# The URL includes: ?sslmode=require&channel_binding=require
# These are required for Neon

# If still failing, try removing channel_binding temporarily:
# url: jdbc:postgresql://...neondb?sslmode=require

# Then add back after testing:
# url: jdbc:postgresql://...neondb?sslmode=require&channel_binding=require
```

**Solution D: Connection Pool Issues**

```yaml
# Add to application.yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      connection-timeout: 20000
      idle-timeout: 300000
      max-lifetime: 1200000
```

---

### Problem 3: Frontend Can't Reach Backend

**Symptoms:**
```
"Failed to fetch assessments"
Network request shows error 0 (net::ERR_CONNECTION_REFUSED)
Browser console: "localhost:8087 refused to connect"
Frontend shows "Using mock data"
```

**Checklist:**

```javascript
// 1. Check if backend running
debugAPI()  // Should show "Backend: HEALTHY"

// 2. Check API URL
console.log('API URL:', import.meta.env.VITE_API_URL)
// Should show: http://localhost:8087

// 3. Check if can reach it
fetch('http://localhost:8087/api/assessments')
  .then(r => r.json())
  .then(d => console.log('✅ Backend responsive:', d))
  .catch(e => console.log('❌ Backend error:', e.message))
```

**Solution A: Backend Not Running**

```powershell
# Terminal showing backend should say:
# "Started AssessmentServiceApplication in X.XXX seconds"
# "Tomcat started on port(s): 8087"

# If not, start it:
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service
mvn spring-boot:run
```

**Solution B: API URL Wrong in Frontend**

```typescript
// Check src/lib/api.ts
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8087'

// If not localhost:8087, change env:
// .env.development
VITE_API_URL=http://localhost:8087

// Then restart frontend: npm run dev
```

**Solution C: CORS Not Configured**

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
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

Then rebuild:
```powershell
mvn clean package -DskipTests
mvn spring-boot:run
```

**Solution D: Frontend Not Restarted After Config Change**

```powershell
# Stop frontend (Ctrl+C)
# Clear browser cache (Ctrl+Shift+Delete)
# Restart frontend
npm run dev
```

---

### Problem 4: Database Has No Data

**Symptoms:**
```
Frontend shows empty list
curl http://localhost:8087/api/assessments returns []
```

**Check Data in Database:**

```powershell
# Connect to database
psql 'postgresql://neondb_owner:npg_wp8Te4WSLvGP@ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'

# Show tables
\dt

# Check assessment table
SELECT COUNT(*) FROM assessment_table;

# If count is 0, table is empty
# Exit
\q
```

**Solutions:**

**Option A: Create Test Data in Frontend**

```
1. Open http://localhost:5173
2. Click "New Assessment"
3. Fill in form
4. Click "Create"
5. Data should appear in database
```

**Option B: Insert Test Data via API**

```powershell
# Create test assessment
$body = @{
    assessmentCode = "TEST-001"
    incidentId = 1
    assessorId = 123
    assessorName = "Test User"
    severity = "HIGH"
    findings = "Test findings"
    recommendations = "Test recommendations"
    requiredActions = @("RESCUE")
    estimatedCasualties = 5
    estimatedDisplaced = 20
    affectedInfrastructure = "Downtown"
} | ConvertTo-Json

curl -X POST http://localhost:8087/api/assessments `
  -Headers @{"Content-Type"="application/json"} `
  -Body $body
```

**Option C: Insert Directly via SQL**

```sql
-- Connect to database
-- psql 'postgresql://...'

INSERT INTO assessment_table (
    assessment_code, incident_id, assessor_id, assessor_name,
    severity, findings, recommendations, estimated_casualties
) VALUES (
    'SQL-TEST-001', 1, 999, 'SQL User',
    'HIGH', 'Test findings', 'Test recommendations', 10
);

-- Verify
SELECT * FROM assessment_table;

-- Exit
\q
```

**Option D: Check Hibernatescrash Auto-Create Tables**

```yaml
# In application.yaml, ensure:
spring:
  jpa:
    hibernate:
      ddl-auto: update  # or create if tables missing

# Restart backend if changed
```

---

### Problem 5: Data Not Syncing Between Frontend and Database

**Symptoms:**
```
Create in frontend, doesn't appear in database
Database has data, frontend shows empty
Changes don't persist after reload
```

**Diagnosis:**

```javascript
// Frontend console
// 1. Check if POST is working
const result = await assessmentAPI.createAssessment({...})
console.log('Response:', result)  // Should show "id" if created

// 2. Check if GET is working
const data = await assessmentAPI.getAllAssessments()
console.log('Data:', data)  // Should show created item

// 3. Check if mock data is being used
const health = await getBackendHealth()
console.log('Using mock?', health.useMockData)  // Should be false
```

**SQL:**

```sql
-- Check if data actually in database
SELECT * FROM assessment_table WHERE assessment_code = 'TEST-001';

-- If not found, database isn't receiving data
-- If found, frontend isn't reading it
```

**Solutions:**

**Solution A: Backend Not Persisting Data**

```java
// Check Entity has JPA annotations
@Entity
@Table(name = "assessment_table")
@DynamicUpdate
@DynamicInsert
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "assessment_code")
    private String assessmentCode;
    
    // ... other fields
}
```

**Solution B: Frontend Using Mock Data**

```javascript
// Check if usesMock data
debugAPI()

// If shows: useMockData: true
// Backend isn't running or not reachable

// Fix: Restart backend
mvn spring-boot:run
```

**Solution C: Transactions Not Committing**

```java
// Check service layer has @Transactional
@Service
@Transactional
public class AssessmentService {
    public Assessment create(Assessment asst) {
        return assessmentRepository.save(asst);
    }
}
```

**Solution D: Connection Lost mid-transaction**

```yaml
# Increase connection timeout and cleanup interval
spring:
  datasource:
    hikari:
      connection-timeout: 30000  # 30 seconds
      idle-timeout: 600000       # 10 minutes
      validation-query: "SELECT 1"
```

---

### Problem 6: API Endpoints Not Working

**Symptoms:**
```
GET http://localhost:8087/api/assessments → 404 Not Found
OR 500 Internal Server Error
OR Different error for each endpoint
```

**Check Endpoints:**

```powershell
# 1. Test GET all
curl http://localhost:8087/api/assessments
# Response: [] or [{},...] 

# 2. Test GET one
curl http://localhost:8087/api/assessments/1
# Response: {id:1, ...}

# 3. Test GET completed
curl http://localhost:8087/api/assessments/completed
# Response: [] or [{...}]

# 4. Test POST create
$body = @{
    assessmentCode = "TEST"
    incidentId = 1
    assessorId = 1
    assessorName = "Test"
} | ConvertTo-Json

curl -X POST http://localhost:8087/api/assessments `
  -Headers @{"Content-Type"="application/json"} `
  -Body $body
# Response: {id: X, ...}

# 5. Show error details
# If 404/500, check terminal for stack trace in backend
```

**Solution A: Controller Not Mapped**

```java
// Check @RequestMapping
@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {
    
    @GetMapping
    public List<Assessment> getAll() { ... }
    
    @PostMapping
    public Assessment create(@RequestBody Assessment a) { ... }
}
```

**Solution B: Entity Class Mismatch**

```java
// Ensure @Table name matches database
@Table(name = "assessment_table")  // Match DB table name
public class Assessment { ... }
```

**Solution C: Missing Dependencies**

```powershell
# Rebuild to get all dependencies
mvn clean package -DskipTests

# Check target/lib for spring-web, spring-data-jpa, etc.
```

**Solution D: Port Configuration Override**

```powershell
# Check environment variables might override port
set VITE_API_URL=http://otherhost:9000

# If set, unset it:
$env:VITE_API_URL = ""

# Or fix to correct value:
$env:VITE_API_URL = "http://localhost:8087"
```

---

## 🧪 Full Connection Test Script

Save as `test-connection.ps1`:

```powershell
Write-Host "🔍 Testing Backend-Database-Frontend Connection`n"

# 1. Check Java
Write-Host "1️⃣ Checking Java..."
try {
    $java = java -version 2>&1
    Write-Host "✅ Java: $($java.Split('"')[1])`n"
} catch {
    Write-Host "❌ Java not found`n"
}

# 2. Check backend port
Write-Host "2️⃣ Checking Backend Port (8087)..."
try {
    $conn = Get-Process java -ErrorAction Stop
    Write-Host "✅ Java process running`n"
} catch {
    Write-Host "⚠️ Java process not found`n"
}

# 3. Test API endpoint
Write-Host "3️⃣ Testing API Endpoint..."
try {
    $response = curl -s http://localhost:8087/api/assessments
    if ($response) {
        Write-Host "✅ Backend responding`n"
    } else {
        Write-Host "❌ No response from backend`n"
    }
} catch {
    Write-Host "❌ Cannot reach backend: $($_.Exception.Message)`n"
}

# 4. Test database
Write-Host "4️⃣ Testing Database Connection..."
# Note: Requires psql installed
# psql 'postgresql://...' -c "SELECT COUNT(*) FROM assessment_table;"

# 5. Frontend test
Write-Host "5️⃣ Frontend (must be running on http://localhost:5173)"
Write-Host "   Open browser and run: debugAPI()`n"

Write-Host "Done!`n"
```

Run it:
```powershell
.\test-connection.ps1
```

---

## ✅ Success Indicators

Check these logs when everything is working:

**Backend Console:**
```
Started AssessmentServiceApplication in 7.234 seconds
Tomcat started on port(s): 8087 (http)
Hibernate: select ... from assessment_table
```

**Frontend Console:**
```
[API] GET http://localhost:8087/api/assessments
[API] Response 200 [...]
```

**Dashboard:**
- Shows list of assessments from database
- Can create new assessments
- Can edit and delete
- No red error borders

---

## 📞 If Still Not Working

1. **For Backend Issues:**
   - Check: `C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service\BACKEND_STARTUP_GUIDE.md`

2. **For Database Issues:**
   - Check: `C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\frontend\DATABASE_CONNECTION_GUIDE.md`

3. **For Frontend Issues:**
   - Check: `C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\frontend\DEBUGGING_GUIDE.md`

4. **For all issues:**
   - Run `debugAPI()` in browser console
   - Check DevTools Network tab for failed requests
   - Check backend terminal for error messages

---

**Last Updated:** February 27, 2026  
**Status:** Comprehensive Troubleshooting Ready ✅
