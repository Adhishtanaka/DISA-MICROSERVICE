# 🔧 API Testing & CRUD Operations Guide

## Quick Diagnosis

Open browser console and run:
```javascript
debugAPI()
```

This will display your API connection status, mock data status, and latency.

---

## ✅ CRUD Commands Explained

The frontend implements all standard CRUD operations:

### 1. CREATE - Add New Data
**HTTP Method:** `POST`  
**Use Case:** Creating new assessments

```
POST /api/assessments
Body: Assessment object
Response: Created assessment with ID
```

**Frontend Usage:**
```typescript
const response = await assessmentAPI.createAssessment({
  assessmentCode: 'ASS-001',
  incidentId: 1,
  // ... other fields
})
```

**Button:** "New Assessment" → Fill form → Click "Create"

---

### 2. READ - Fetch Data
**HTTP Method:** `GET`  
**Use Cases:** Viewing assessments

#### Read All
```
GET /api/assessments
Response: Array of all assessments
```

#### Read One
```
GET /api/assessments/{id}
Response: Single assessment object
```

#### Read Filtered
```
GET /api/assessments/completed
Response: Array of completed assessments only
```

**Frontend Usage:**
```typescript
// Get all
const all = await assessmentAPI.getAllAssessments()

// Get one
const single = await assessmentAPI.getAssessmentById(1)

// Get completed only
const completed = await assessmentAPI.getCompletedAssessments()

// Get by incident
const incident = await assessmentAPI.getAssessmentsByIncident(1)
```

**UI:** Tab navigation or View buttons trigger reads

---

### 3. UPDATE - Modify Data
**HTTP Method:** `PUT`  
**Use Case:** Editing assessment details

```
PUT /api/assessments/{id}
Body: Updated fields
Response: Updated assessment
```

**Frontend Usage:**
```typescript
const response = await assessmentAPI.updateAssessment(1, {
  findings: 'Updated findings',
  severity: 'SEVERE'
  // ... other fields to update
})
```

**Button:** Edit icon (pencil) → Modify form → Click "Update"

---

### 4. COMPLETE - Special UPDATE
**HTTP Method:** `PUT`  
**Use Case:** Mark assessment as complete (publishes event)

```
PUT /api/assessments/{id}/complete
Response: Completed assessment
Event: Published to message queue
```

**Frontend Usage:**
```typescript
const response = await assessmentAPI.completeAssessment(1)
```

**Button:** "Complete Assessment" in Actions tab

---

### 5. DELETE - Remove Data
**HTTP Method:** `DELETE`  
**Use Case:** Removing assessments

```
DELETE /api/assessments/{id}
Response: 204 No Content
```

**Frontend Usage:**
```typescript
await assessmentAPI.deleteAssessment(1)
```

**Button:** Trash icon → Confirm → Deleted

---

### 6. CREATE (FILE) - Upload Photos
**HTTP Method:** `POST`  
**Use Case:** Adding photos to assessment

```
POST /api/assessments/{id}/photos
Body: FormData with file
Response: Assessment with new photo
```

**Frontend Usage:**
```typescript
const formData = new FormData()
formData.append('file', file)
const response = await assessmentAPI.uploadPhoto(1, formData)
```

**UI:** Drag-drop or select in Photos tab

---

### 7. READ (FILE) - Download Photos
**HTTP Method:** `GET`  
**Use Case:** Retrieving stored photos

```
GET /api/assessments/photos/{filename}
Response: Image blob
```

---

## 🧪 Testing CRUD Operations

### Using Browser Console

```javascript
// Test CREATE
async function testCreate() {
  try {
    const result = await assessmentAPI.createAssessment({
      assessmentCode: 'TEST-001',
      incidentId: 1,
      assessorId: 999,
      assessorName: 'Test User',
      severity: 'MODERATE',
      findings: 'Test findings',
      recommendations: 'Test recommendations',
      requiredActions: ['RESCUE'],
      estimatedCasualties: 5,
      estimatedDisplaced: 20,
      affectedInfrastructure: 'Test area'
    })
    console.log('✅ CREATE Success:', result.data)
  } catch (err) {
    console.error('❌ CREATE Failed:', err)
  }
}
testCreate()

// Test READ
async function testRead() {
  try {
    const result = await assessmentAPI.getAllAssessments()
    console.log('✅ READ Success:', result.data)
  } catch (err) {
    console.error('❌ READ Failed:', err)
  }
}
testRead()

// Test UPDATE
async function testUpdate() {
  try {
    const result = await assessmentAPI.updateAssessment(1, {
      findings: 'Updated test findings'
    })
    console.log('✅ UPDATE Success:', result.data)
  } catch (err) {
    console.error('❌ UPDATE Failed:', err)
  }
}
testUpdate()

// Test COMPLETE
async function testComplete() {
  try {
    const result = await assessmentAPI.completeAssessment(1)
    console.log('✅ COMPLETE Success:', result.data)
  } catch (err) {
    console.error('❌ COMPLETE Failed:', err)
  }
}
testComplete()

// Test DELETE
async function testDelete() {
  try {
    const result = await assessmentAPI.deleteAssessment(1)
    console.log('✅ DELETE Success')
  } catch (err) {
    console.error('❌ DELETE Failed:', err)
  }
}
testDelete()

// Run all tests
async function runAllTests() {
  await testCreate()
  await testRead()
  await testUpdate()
  await testComplete()
  await testDelete()
}
runAllTests()
```

---

## 🔍 Error Diagnosis

### Error: "Failed to fetch assessments"

**Possible Causes:**
1. ❌ Backend not running
2. ❌ Wrong API URL
3. ❌ CORS not enabled
4. ❌ Network firewall

**Solutions:**

**Step 1: Check Backend Status**
```javascript
debugAPI()
// Look for "isHealthy" status
```

**Step 2: Verify API URL**
```javascript
console.log('API URL:', (await getBackendHealth()).apiUrl)
// Should be: http://localhost:8087
```

**Step 3: Check Network**
```javascript
// In DevTools Network tab, look for failed requests
// Check HTTP status codes:
// - 200: Success
// - 400: Bad request
// - 401: Unauthorized
// - 404: Not found
// - 500: Server error
// - 0: Network error (no connection)
```

**Step 4: Check CORS Headers**
```javascript
// In DevTools, check "Response Headers" for:
// Access-Control-Allow-Origin: *
```

### Error: "No response from server"

**Solutions:**
1. Ensure backend is running: `http://localhost:8087`
2. Check port 8087 is open
3. Check firewall settings
4. Verify .env file has correct `VITE_API_URL`

### Error during CREATE/UPDATE

**Check:**
1. All required fields filled
2. Field types correct
3. No validation errors
4. Form shows error message

---

## 📊 API Response Formats

### Success Response (200)
```json
{
  "id": 1,
  "assessmentCode": "ASS-001",
  "status": "DRAFT",
  // ... other fields
}
```

### Error Response (4xx/5xx)
```json
{
  "message": "Error description",
  "status": 400,
  "error": "validation_error"
}
```

---

## 🔌 Backend Requirements Checklist

For the backend to work properly:

- [ ] Running on port 8087
- [ ] CORS headers configured
- [ ] Endpoints implemented:
  - [ ] `POST /api/assessments`
  - [ ] `GET /api/assessments`
  - [ ] `GET /api/assessments/{id}`
  - [ ] `GET /api/assessments/completed`
  - [ ] `PUT /api/assessments/{id}`
  - [ ] `PUT /api/assessments/{id}/complete`
  - [ ] `POST /api/assessments/{id}/photos`
  - [ ] `DELETE /api/assessments/{id}`
- [ ] JWT authentication (if enabled)
- [ ] Database connected
- [ ] Photo upload directory exists

---

## 🛠️ Mock Data Mode

When backend is unavailable, the frontend uses mock data automatically.

### Checking Mock Status
```javascript
const status = await getBackendHealth()
console.log('Using Mock Data:', status.useMockData)
console.log('Mock Assessments:', status.mockDataCount)
```

### Mock Data Generated
- 2 sample assessments
- Full CRUD operations work
- Photos use blob URLs
- Perfect for testing frontend without backend

---

## 📈 API Logging

Enable detailed logging:

```javascript
// Automatic in development mode
// Shows in browser console

// Examples:
// [API] POST http://localhost:8087/api/assessments
// [API] Response 200 {...}
```

---

## 🧪 Integration Testing Workflow

### Test Sequence

1. **Start Frontend**
   ```bash
   npm run dev
   ```

2. **Open Console**
   - Press F12
   - Go to Console tab

3. **Run Diagnostics**
   ```javascript
   debugAPI()
   ```

4. **Test Each CRUD Operation**
   ```javascript
   // Create
   const newId = (await assessmentAPI.createAssessment({...})).data.id
   
   // Read
   await assessmentAPI.getAssessmentById(newId)
   
   // Update
   await assessmentAPI.updateAssessment(newId, {...})
   
   // Complete
   await assessmentAPI.completeAssessment(newId)
   
   // Delete
   await assessmentAPI.deleteAssessment(newId)
   ```

5. **Verify in UI**
   - Check dashboard updates
   - Check filters work
   - Check modals open/close

---

## 📝 Common CRUD Patterns

### Create & Read Flow
```javascript
// Create
const response = await assessmentAPI.createAssessment(data)
const newId = response.data.id

// Read created item
const item = await assessmentAPI.getAssessmentById(newId)
console.log('Item exists:', item.data.id === newId)
```

### Update & Verify
```javascript
// Update
await assessmentAPI.updateAssessment(id, { findings: 'Updated' })

// Verify update
const updated = await assessmentAPI.getAssessmentById(id)
console.log('Updated:', updated.data.findings === 'Updated')
```

### List & Filter
```javascript
// Get all
const all = await assessmentAPI.getAllAssessments()
console.log('Total:', all.data.length)

// Get completed only
const completed = await assessmentAPI.getCompletedAssessments()
console.log('Completed:', completed.data.length)
```

---

## 🚀 Performance Monitoring

```javascript
// Measure API response time
const start = performance.now()
await assessmentAPI.getAllAssessments()
const latency = performance.now() - start
console.log(`Response time: ${latency.toFixed(2)}ms`)
```

---

## ✅ Troubleshooting Checklist

- [ ] Backend running on port 8087
- [ ] Frontend running on port 5173
- [ ] VITE_API_URL in .env is correct
- [ ] No console errors
- [ ] Network tab shows requests
- [ ] Mock data working
- [ ] All CRUD operations respond
- [ ] Photos upload successfully
- [ ] Form validation working
- [ ] Error messages display

---

## 📞 Getting Help

1. **Check console**: `debugAPI()`
2. **Check Network tab**: DevTools → Network
3. **Check backend logs**: Look for API errors
4. **Check .env file**: Verify API URL
5. **Try mock mode**: Auto-activates when backend unavailable

---

## 🎓 Additional Resources

- [README.md](./README.md) - Full documentation
- [SETUP.md](./SETUP.md) - Quick start
- [FEATURES.md](./FEATURES.md) - Feature details
- Backend API: See port 8087

---

**Last Updated:** February 27, 2026  
**Status:** Testing ready ✅
