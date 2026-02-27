# ⚡ Quick Start: Connect Database → Backend → Frontend (5 Minutes)

## 📋 Prerequisites Checklist

Before you start, verify these are installed:

```powershell
# Check Java 17+
java -version
# Should show: openjdk 17.x.x

# Check Maven
mvn -version
# Should show: Apache Maven 3.8.x

# Check Node.js
node -version
# Should show: v20.x.x

# Check npm
npm -version
# Should show: 10.x.x
```

**Missing something?** Run:
```powershell
choco install openjdk17 maven nodejs -y
# Then restart PowerShell
```

---

## 🚀 Step 1: Start the Backend (2 minutes)

```powershell
# 1. Open PowerShell Terminal
# 2. Navigate to backend
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service

# 3. Build and run
mvn clean package -DskipTests
mvn spring-boot:run

# ⏳ Wait until you see:
# "Started AssessmentServiceApplication in X.XXX seconds"
# "Tomcat started on port(s): 8087"
```

**✅ Backend Started**  
Keep this terminal open. Don't press Ctrl+C.

**If it fails:**
```powershell
# Check port 8087 available
netstat -ano | findstr :8087

# Check Java working
java -version
```

---

## 🎨 Step 2: Start the Frontend (1 minute)

```powershell
# 1. Open new PowerShell Terminal (don't close backend terminal)
# 2. Navigate to frontend
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\frontend

# 3. Install dependencies (if needed)
npm install

# 4. Start development server
npm run dev

# ⏳ Wait until you see:
# "Local: http://localhost:5173/"
```

**✅ Frontend Started**  
Keep this terminal open too.

---

## 🔗 Step 3: Verify Connection (1 minute)

### In Browser

```
1. Open http://localhost:5173
2. Wait for page to load
3. Look at Dashboard - should show assessments (or empty list)
```

### In Browser Console

```javascript
// Press F12 to open DevTools
// Go to Console tab
// Paste and run:

debugAPI()

// Should show:
// ✅ Backend Connection: HEALTHY
// ✅ API URL: http://localhost:8087
// ✅ Using Mock Data: false
// ✅ Test Call Latency: XXXms
```

**✅ Everything Connected!**

---

## ✨ Step 4: Test CRUD Operations (1 minute)

### Create New Assessment

**In UI:**
```
1. Click "New Assessment" button
2. Fill in form:
   - Assessment Code: TEST-001
   - Incident ID: 1
   - Assessor Name: Test User
   - Severity: HIGH
   - Findings: Test assessment
   - Recommendations: Test recommendation
3. Click "Create"
```

**Check it worked:**
```javascript
// In browser console
await assessmentAPI.getAllAssessments()
// Should show your new assessment

// Or check database
// Open another terminal and run:
psql 'postgresql://neondb_owner:npg_wp8Te4WSLvGP@ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'

SELECT * FROM assessment_table;

\q
```

### Update Assessment

```
1. Click edit (pencil icon) on your assessment
2. Change findings to "Updated test findings"
3. Click "Update"
```

### Delete Assessment

```
1. Click delete (trash icon) on your assessment
2. Confirm deletion
```

### Read/View

```
1. Assessment appears in list
2. Click on it to view details
3. Data shows from database
```

**✅ All Operations Working!**

---

## 🎯 Full Connection Stack Picture

```
Your Database (Neon PostgreSQL)
  ↑ JDBC Connection (read/write)
  ↓
Backend (Spring Boot on port 8087)
  ↑ HTTP/REST API
  ↓
Frontend (React on port 5173)
  ↑ User Interface
  ↓
You (Using the app)
```

---

## 📊 Expected Files & Logs

### Backend Terminal Should Show:

```
[INFO] Downloading...
[INFO] Building assessment-service
[INFO] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
...
Hibernate: select ... from assessment_table
...
o.s.b.a.s.s.SecurityFilterChain: Spring Security web Configuration disabled
o.s.s.c.h.AssessmentServiceApplication: Started AssessmentServiceApplication in 8.234 seconds
o.s.s.c.h.AssessmentServiceApplication: Tomcat started on port(s): 8087 (http)
```

### Frontend Terminal Should Show:

```
VITE v7.3.1  ready in 456 ms

➜  Local:   http://localhost:5173/
➜  press h to show help
```

### Browser Console Should Show:

```
✅ Backend health check passed
✅ Using real API (not mock data)
🔧 Detailed logging enabled...
[API] GET http://localhost:8087/api/assessments
[API] Response 200 [...]
```

---

## 🆘 Quick Fixes (If Something's Wrong)

### Backend Won't Start

```powershell
# Kill any Java process on 8087
taskkill /F /IM java.exe

# Then start fresh
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service
mvn clean package -DskipTests
mvn spring-boot:run
```

### Frontend Shows Blank Page

```powershell
# Stop frontend (Ctrl+C)
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\frontend
npm install  # Fresh install
npm run dev  # Restart
```

### "Failed to fetch assessments" Error

```javascript
// In browser console
debugAPI()

// If shows: useMockData: true
// Backend is not running - go to Step 1

// If shows: Connection error
// Check backend terminal for errors
```

### Database has No Data

```
1. In frontend, create test assessment
2. Wait for success message
3. Check backend logs for SQL insert
4. Refresh page - data should appear
```

---

## 🔍 Verification Checklist

Before considering it complete:

- [ ] Backend terminal shows "Started AssessmentServiceApplication"
- [ ] Frontend terminal shows "Local: http://localhost:5173/"
- [ ] http://localhost:5173 loads without errors
- [ ] Browser console has no red errors
- [ ] `debugAPI()` shows "Backend: HEALTHY"
- [ ] Can create new assessment
- [ ] New assessment appears in list
- [ ] Can edit assessment
- [ ] Can delete assessment
- [ ] Can view assessment details

All checked? ✅ **You're done!**

---

## 📚 What's Connected?

### Database
- Type: PostgreSQL (Neon)
- Host: ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech
- Database: neondb
- Table: assessment_table
- Status: ✅ Hosted (always on)

### Backend
- Type: Spring Boot Java
- Port: 8087
- Base URL: http://localhost:8087
- API Prefix: /api/assessments
- Status: ✅ Running locally

### Frontend  
- Type: React + TypeScript
- Port: 5173
- URL: http://localhost:5173
- Status: ✅ Running locally

### Connection Flow
```
User Types → Frontend :5173 → Backend :8087 → Database (Neon)
Browser                |              |
Shows results ← API Response ← SQL Query
```

---

## 🚀 Next Steps

After everything is working:

1. **Add More Data**
   - Create multiple assessments
   - Add photos
   - Test filters

2. **Explore Features**
   - Check report generation
   - Test photo upload
   - Try completion workflow

3. **Prepare for Production**
   - Read: `DEPLOYMENT.md`
   - Configure production database
   - Set up domain/HTTPS

4. **Development**
   - Make code changes
   - Changes auto-reload (frontend)
   - Rebuild backend for changes

---

## 🔗 Key Commands Reference

Keep these handy:

```powershell
# Backend (Run in Terminal 1)
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\backend\assessment-service
mvn spring-boot:run

# Frontend (Run in Terminal 2)
cd C:\Users\U\Documents\GitHub\DISA-MICROSERVICE\frontend
npm run dev

# Test API (Run in Terminal 3)
curl http://localhost:8087/api/assessments

# Database connection
psql 'postgresql://neondb_owner:npg_wp8Te4WSLvGP@ep-morning-dust-ai82f4ju-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require'
```

---

## 📞 Troubleshooting Links

If something doesn't work:

1. **Backend issues** → `backend/assessment-service/BACKEND_STARTUP_GUIDE.md`
2. **Database issues** → `frontend/DATABASE_CONNECTION_GUIDE.md`
3. **Frontend issues** → `frontend/DEBUGGING_GUIDE.md`
4. **Complete troubleshooting** → `INTEGRATION_TROUBLESHOOTING.md`

---

## ⏱️ Time Summary

| Step | Time | Status |
|------|------|--------|
| Prerequisites | 2 min | Already done? |
| Start Backend | 2 min | mvn spring-boot:run |
| Start Frontend | 1 min | npm run dev |
| Verify Connection | 1 min | debugAPI() |
| Test CRUD | 1 min | Create/Read/Update/Delete |
| **TOTAL** | **≈5 min** | ✅ Complete |

---

## 🎉 Success Indicators

You'll see:

✅ Backend console shows: "Started AssessmentServiceApplication"  
✅ Frontend console shows: "Local: http://localhost:5173/"  
✅ Browser shows: Dashboard with assessments  
✅ No red errors in console  
✅ Create new assessment works  
✅ Data persists in database  

**When all ✅ = System is fully connected and working!**

---

**Status:** Ready to connect ⚡  
**Last Updated:** February 27, 2026
