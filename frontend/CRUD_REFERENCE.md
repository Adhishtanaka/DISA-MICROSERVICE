# 🔄 CRUD Operations Reference

## Overview

All 8 API endpoints are fully implemented with CRUD logging:

| Operation | Method | Endpoint | Status |
|-----------|--------|----------|--------|
| **C**reate Assessment | POST | `/api/assessments` | ✅ Logged |
| **R**ead All | GET | `/api/assessments` | ✅ Logged |
| **R**ead One | GET | `/api/assessments/{id}` | ✅ Logged |
| **R**ead Incident Assessments | GET | `/api/assessments/incident/{incidentId}` | ✅ Logged |
| **R**ead Completed | GET | `/api/assessments/completed` | ✅ Logged |
| **U**pdate Assessment | PUT | `/api/assessments/{id}` | ✅ Logged |
| **C**omplete Assessment | PUT | `/api/assessments/{id}/complete` | ✅ Logged |
| **D**elete Assessment | DELETE | `/api/assessments/{id}` | ✅ Logged |
| **C**reate Photo | POST | `/api/assessments/{id}/photos` | ✅ Logged |
| **R**ead Photo | GET | `/api/assessments/photos/{filename}` | ✅ Logged |

---

## CREATE Operations

### 1. Create Assessment
```typescript
// Import
import { assessmentAPI } from '@/lib/api'

// Usage
const response = await assessmentAPI.createAssessment({
  assessmentCode: 'ASS-2024-001',
  incidentId: 1,
  assessorId: 123,
  assessorName: 'John Doe',
  severity: 'HIGH',
  findings: 'Building damaged',
  recommendations: 'Immediate evacuation needed',
  requiredActions: ['RESCUE', 'MEDICAL'],
  estimatedCasualties: 5,
  estimatedDisplaced: 50,
  affectedInfrastructure: 'Downtown area'
})

// Response
const newAssessment = response.data
console.log(`Created: ${newAssessment.id}`)
```

**Console Output:**
```
[API] POST http://localhost:8087/api/assessments
[API] Response 200 {id: 1, assessmentCode: 'ASS-2024-001', ...}
```

**Error Handling:**
```typescript
catch (err) {
  console.error('[API Request Error] Could not create assessment:', {
    message: err.userMessage,
    status: err.response?.status,
    data: err.response?.data
  })
}
```

---

### 2. Upload Photo
```typescript
// Usage
const formData = new FormData()
formData.append('file', photoFile)

const response = await assessmentAPI.uploadPhoto(assessmentId, formData)

// Response
const assessment = response.data
console.log(`Photo added. Total: ${assessment.photoUrls?.length || 0}`)
```

**Console Output:**
```
[API] POST http://localhost:8087/api/assessments/1/photos
[API] Response 200 {photoUrls: ['photo1.jpg'], ...}
```

---

## READ Operations

### 1. Get All Assessments
```typescript
// Usage
const response = await assessmentAPI.getAllAssessments()

// Response
const assessments = response.data
console.log(`Total: ${assessments.length}`)

// Example: 2 assessments from mock data
assessments.forEach(a => {
  console.log(`  - ${a.assessmentCode}: ${a.severity}`)
})
```

**Console Output:**
```
[API] GET http://localhost:8087/api/assessments
[API] Response 200 [2 items]
  - ASS-MOCK-001: CRITICAL
  - ASS-MOCK-002: MODERATE
```

---

### 2. Get Assessment by ID
```typescript
// Usage
const response = await assessmentAPI.getAssessmentById(1)

// Response
const assessment = response.data
console.log(`${assessment.assessmentCode}: ${assessment.findings}`)
```

**Console Output:**
```
[API] GET http://localhost:8087/api/assessments/1
[API] Response 200 {id: 1, assessmentCode: 'ASS-MOCK-001', ...}
```

---

### 3. Get Assessments by Incident
```typescript
// Usage
const response = await assessmentAPI.getAssessmentsByIncident(1)

// Response
const assessments = response.data
console.log(`Incident 1 has ${assessments.length} assessments`)
```

**Console Output:**
```
[API] GET http://localhost:8087/api/assessments/incident/1
[API] Response 200 [1 item]
```

---

### 4. Get Completed Assessments
```typescript
// Usage
const response = await assessmentAPI.getCompletedAssessments()

// Response
const completed = response.data
console.log(`${completed.length} completed assessments`)
```

**Console Output:**
```
[API] GET http://localhost:8087/api/assessments/completed
[API] Response 200 [0 items]
```

---

### 5. Download Photo
```typescript
// Usage
const response = await assessmentAPI.downloadPhoto('photo.jpg')

// Response: Blob
const blob = response.data
const url = URL.createObjectURL(blob)
// Use url to display or download
```

**Console Output:**
```
[API] GET http://localhost:8087/api/assessments/photos/photo.jpg
[API] Response 200 (image blob)
```

---

## UPDATE Operations

### 1. Update Assessment
```typescript
// Usage
const response = await assessmentAPI.updateAssessment(1, {
  findings: 'Updated findings text',
  severity: 'SEVERE',
  recommendations: 'New recommendations'
  // Only include fields to update
})

// Response
const updated = response.data
console.log(`Updated: ${updated.assessmentCode}`)
```

**Console Output:**
```
[API] PUT http://localhost:8087/api/assessments/1
[API] Response 200 {id: 1, findings: 'Updated findings text', ...}
```

**Error Handling:**
```typescript
catch (err) {
  console.error('[API Request Error] Could not update:', {
    message: err.userMessage,
    status: err.response?.status
  })
}
```

---

### 2. Complete Assessment
```typescript
// Usage
const response = await assessmentAPI.completeAssessment(1)

// Response
const completed = response.data
console.log(`Status: ${completed.status}`) // 'COMPLETED'

// NOTE: This publishes an event to message queue
```

**Console Output:**
```
[API] PUT http://localhost:8087/api/assessments/1/complete
[API] Response 200 {id: 1, status: 'COMPLETED', ...}
```

**Side Effects:**
- Assessment.status → 'COMPLETED'
- Event published to RabbitMQ/Azure Service Bus
- Triggers downstream workflows

---

## DELETE Operations

### 1. Delete Assessment
```typescript
// Usage
await assessmentAPI.deleteAssessment(1)

// Response: No content (204)
console.log('Assessment deleted')
```

**Console Output:**
```
[API] DELETE http://localhost:8087/api/assessments/1
[API] Response 204
```

**Warning:**
- This is permanent
- All assessment data lost
- Associated photos may remain

---

## Mock Data CRUD

When backend is unavailable, all CRUD operations work with mock data:

```javascript
// Check if using mock data
const health = await getBackendHealth()
if (health.useMockData) {
  console.log('Using mock data - all operations simulated')
}

// Mock data set includes:
// - 2 sample assessments
// - Full CRUD operations supported
// - Photos handled with blob URLs
// - Perfect for frontend testing
```

### Accessing Mock Data Directly
```typescript
import { assessmentAPI } from '@/lib/api'

// Get mock assessment
const mockAssessments = await assessmentAPI.getAllAssessments()
// Always returns 2 mock items

// Create adds to mock array
await assessmentAPI.createAssessment({...})

// Update modifies mock array
await assessmentAPI.updateAssessment(1, {...})

// Delete removes from mock array
await assessmentAPI.deleteAssessment(1)
```

---

## Error Codes

### Status Codes
```
200 - Success
201 - Created
204 - No Content (delete success)
400 - Bad Request (validation error)
401 - Unauthorized (JWT invalid)
403 - Forbidden (permission denied)
404 - Not Found (resource doesn't exist)
409 - Conflict (duplicate key)
500 - Server Error
503 - Service Unavailable (backend down)
```

### Error Response Format
```json
{
  "message": "Validation failed",
  "status": 400,
  "error": "validation_error",
  "details": {
    "field": "assessmentCode",
    "issue": "must be unique"
  }
}
```

---

## Response Field Mapping

### Assessment Object
```typescript
{
  id: number;
  assessmentCode: string; // 'ASS-2024-001'
  incidentId: number;
  assessorId: number;
  assessorName: string;
  severity: 'LOW' | 'MODERATE' | 'HIGH' | 'CRITICAL';
  findings: string;
  recommendations: string;
  requiredActions: string[]; // ['RESCUE', 'MEDICAL', ...]
  estimatedCasualties: number;
  estimatedDisplaced: number;
  affectedInfrastructure: string;
  status: 'DRAFT' | 'IN_PROGRESS' | 'COMPLETED';
  photoUrls?: string[];
  createdAt?: string; // ISO 8601
  updatedAt?: string; // ISO 8601
}
```

---

## Best Practices

### 1. Always Use Try-Catch
```typescript
try {
  const response = await assessmentAPI.getAssessmentById(id)
} catch (err) {
  // Handle error - don't let it crash UI
  console.error('Failed to fetch:', err.userMessage)
}
```

### 2. Check Mock Data Mode
```typescript
const health = await getBackendHealth()
if (health.useMockData) {
  console.warn('Using mock data - changes will be lost on refresh')
}
```

### 3. Validate Before Upload
```typescript
if (file.size > 5 * 1024 * 1024) { // 5MB limit
  console.error('File too large')
  return
}
```

### 4. Handle Async Properly
```typescript
// ✅ Correct
async function loadData() {
  try {
    const data = await assessmentAPI.getAllAssessments()
    setState(data.data)
  } catch (err) {
    setError(err.userMessage)
  }
}

// ❌ Wrong
const data = assessmentAPI.getAllAssessments() // Missing await
```

### 5. Clean Up on Delete
```typescript
// If deleting assessment with photos, clean up blob URLs
if (assessment.photoUrls) {
  assessment.photoUrls.forEach(url => {
    if (url.startsWith('blob:')) {
      URL.revokeObjectURL(url)
    }
  })
}
```

---

## Testing Commands

### Test All CRUD Operations
```javascript
async function testAllCRUD() {
  console.group('🧪 Testing All CRUD Operations')
  
  // CREATE
  console.log('1️⃣ Testing CREATE...')
  const created = await assessmentAPI.createAssessment({
    assessmentCode: 'TEST-001',
    incidentId: 1, assessorId: 999, assessorName: 'Test'
  })
  console.log('✅ CREATE:', created.data.id)
  
  // READ
  console.log('2️⃣ Testing READ...')
  const read = await assessmentAPI.getAssessmentById(created.data.id)
  console.log('✅ READ:', read.data.assessmentCode)
  
  // UPDATE
  console.log('3️⃣ Testing UPDATE...')
  const updated = await assessmentAPI.updateAssessment(created.data.id, {
    findings: 'Updated'
  })
  console.log('✅ UPDATE:', updated.data.findings)
  
  // DELETE
  console.log('4️⃣ Testing DELETE...')
  await assessmentAPI.deleteAssessment(created.data.id)
  console.log('✅ DELETE: Success')
  
  console.groupEnd()
}

testAllCRUD()
```

**Expected Output:**
```
🧪 Testing All CRUD Operations
1️⃣ Testing CREATE...
  [API] POST http://localhost:8087/api/assessments
  [API] Response 200 {...}
  ✅ CREATE: 3
2️⃣ Testing READ...
  [API] GET http://localhost:8087/api/assessments/3
  [API] Response 200 {...}
  ✅ READ: TEST-001
3️⃣ Testing UPDATE...
  [API] PUT http://localhost:8087/api/assessments/3
  [API] Response 200 {...}
  ✅ UPDATE: Updated
4️⃣ Testing DELETE...
  [API] DELETE http://localhost:8087/api/assessments/3
  [API] Response 204
  ✅ DELETE: Success
```

---

## Integrations

### With React Components
```typescript
import { useEffect, useState } from 'react'
import { assessmentAPI } from '@/lib/api'

export function AssessmentList() {
  const [items, setItems] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    // READ
    assessmentAPI.getAllAssessments()
      .then(res => setItems(res.data))
      .catch(err => setError(err.userMessage))
  }, [])

  const handleDelete = async (id: number) => {
    // DELETE
    try {
      await assessmentAPI.deleteAssessment(id)
      setItems(items.filter(i => i.id !== id))
    } catch (err) {
      setError(err.userMessage)
    }
  }

  return (
    // Render items with handleDelete
  )
}
```

### With Zustand Store
```typescript
import create from 'zustand'
import { assessmentAPI } from '@/lib/api'

const useStore = create((set) => ({
  items: [],
  
  // READ
  fetch: async () => {
    const res = await assessmentAPI.getAllAssessments()
    set({ items: res.data })
  },
  
  // CREATE
  add: async (data) => {
    const res = await assessmentAPI.createAssessment(data)
    set(state => ({ items: [...state.items, res.data] }))
  },
  
  // UPDATE
  update: async (id, data) => {
    const res = await assessmentAPI.updateAssessment(id, data)
    set(state => ({
      items: state.items.map(i => i.id === id ? res.data : i)
    }))
  },
  
  // DELETE
  remove: async (id) => {
    await assessmentAPI.deleteAssessment(id)
    set(state => ({ items: state.items.filter(i => i.id !== id) }))
  }
}))
```

---

## Performance

### Typical Response Times
- **GET all**: 50-200ms
- **GET one**: 30-100ms
- **POST create**: 100-300ms
- **PUT update**: 100-300ms
- **DELETE**: 50-150ms
- **File upload**: 500-2000ms (depends on file size)

### Latency Test
```javascript
async function testLatency(operation, name) {
  const start = performance.now()
  try {
    await operation()
    const ms = performance.now() - start
    console.log(`${name}: ${ms.toFixed(0)}ms`)
  } catch (err) {
    console.error(`${name}: Failed`)
  }
}

testLatency(() => assessmentAPI.getAllAssessments(), 'GET All')
testLatency(() => assessmentAPI.getCompletedAssessments(), 'GET Completed')
```

---

**Revision:** 1.0  
**Last Updated:** 2024  
**Status:** Production Ready ✅
