# Assessment Service Frontend - Features Documentation

## 🎯 Dashboard Overview

The main dashboard provides a comprehensive view of all assessment activities with key statistics and quick actions.

### Header Section
- **Service Title**: "Assessment Service" with branding icon
- **Tagline**: "Manage damage assessments and generate follow-up tasks"
- **Create Button**: Primary action to create new assessments

### Statistics Cards
Display real-time metrics:
- **All Assessments**: Total count of all assessments
- **Draft Assessments**: Waiting to be completed
- **Completed Assessments**: Finalized and submitted

### Tab Navigation
Switch between different assessment views:
1. **All Assessments** - View all assessments regardless of status
2. **Draft** - Only incomplete assessments
3. **Completed** - Only submitted and processed assessments

---

## 📝 Assessment List

### View & Manage Assessments

Each assessment card displays:
- **Status Icon**: Visual indicator (draft/completed)
- **Assessment Code**: Unique identifier (e.g., "ASS-201")
- **Severity Badge**: Color-coded damage level
  - Blue: MINOR
  - Yellow: MODERATE
  - Orange: SEVERE
  - Red: CRITICAL
- **Assessor Name**: Who conducted the assessment
- **Incident ID**: Related incident identifier
- **Findings**: Preview of damage assessment
- **Required Actions**: Tags for needed response

### Action Buttons
- **👁️ View**: Open full assessment details
- **✏️ Edit**: Modify draft assessments (disabled for completed)
- **🗑️ Delete**: Remove assessment (with confirmation)

### Filtering Options
```
ALL → Shows all assessments
DRAFT → Only incomplete (status = DRAFT)
COMPLETED → Only finished (status = COMPLETED)
```

### Loading States
- Skeleton loading while fetching data
- Error messages if API calls fail
- Empty state when no assessments exist

---

## ➕ Create New Assessment

### Assessment Form Modal

#### Section 1: Basic Information
- **Assessment Code**: Auto-generated or custom (e.g., "ASS-001")
- **Incident ID**: Reference to the disaster incident
- **Assessor ID**: Assessor's unique identifier
- **Assessor Name**: Full name of the field assessor

#### Section 2: Damage Assessment
- **Severity Level**: Dropdown selection
  - MINOR - Limited damage
  - MODERATE - Significant damage
  - SEVERE - Extensive damage
  - CRITICAL - Catastrophic damage
- **Findings**: Detailed text area for damage description
- **Recommendations**: Suggested mitigation actions
- **Affected Infrastructure**: Types of damage (buildings, roads, etc.)

#### Section 3: Required Actions
Multi-select checklist:
- RESCUE - Victim recovery operations
- MEDICAL_AID - Healthcare/first aid
- DEBRIS_REMOVAL - Cleanup operations
- STRUCTURAL_REPAIR - Building repairs
- UTILITY_RESTORATION - Restore power/water/gas
- EVACUATION - Area evacuation
- SHELTER_PROVISION - Temporary housing
- SANITATION - Hygiene/sanitation facilities

#### Section 4: Impact Estimates
- **Estimated Casualties**: Predicted deaths/injuries
- **Estimated Displaced**: People needing relocation

### Form Features
- **Validation**: All fields required before submission
- **Form State**: Saves to local state until submitted
- **Error Handling**: Shows error messages
- **Loading**: Displays submitting state
- **Cancel Option**: Discard form without saving

---

## 📸 Photo Upload

### Drag & Drop Upload
- Drag image files directly into the upload zone
- Visual feedback on hover (blue highlight)
- Accepts: PNG, JPG, GIF formats
- Max file size: 10MB

### Manual File Selection
- Click "Select File" button
- Choose from file browser
- Only image files accepted

### Uploaded Photos
- **Gallery View**: Grid display of all photos
- **Thumbnail Preview**: Visual representation
- **Hover Effect**: Image icon appears on hover
- **Sequential Display**: Up to all uploaded photos shown

### Photo Management
- Photos auto-save to assessment
- Cannot delete individual photos (limitation)
- Photos only uploaded in DRAFT status
- Accessible from assessment details modal

### Upload Validation
- ✓ File type validation (images only)
- ✓ File size validation (max 10MB)
- ✓ Duplicate upload prevention
- ✓ Upload progress indication
- ✓ Error messages for failures

---

## 👁️ View Assessment Details

### Tabbed Interface

#### Tab 1: Overview
Complete assessment information:

**Status & Severity**
- Current status (DRAFT or COMPLETED)
- Severity level with color coding

**Assessor Information**
- Assessor name and ID
- Incident ID reference

**Assessment Text**
- Detailed findings
- Recommendations provided
- Affected infrastructure list

**Impact Statistics**
- Estimated casualties (red highlight)
- Estimated displaced persons

#### Tab 2: Photos
- Photo gallery of all uploads
- View all assessment images
- Add new photos (DRAFT only)
- Image preview on click

#### Tab 3: Actions
**Required Actions List**
- Bullet-point list of all actions
- Each action clearly displayed
- Complete action list for reference

**Completion Button** (DRAFT only)
- Large green "Complete Assessment" button
- Confirmation dialog before final action
- Publishes event to trigger task creation
- Cannot be undone

**Completion Status** (if COMPLETED)
- Green confirmation badge
- Completion date displayed
- Locked state indicator

### Modal Features
- **Close Button**: X in top right
- **Header**: Code and status badge
- **Color Theme**: Blue gradient header
- **Responsive**: Works on all screen sizes
- **Scroll Area**: Content scrolls if needed

---

## ✏️ Edit Assessment

### Edit Mode
Available only for **DRAFT** assessments

### Editable Fields
- Assessment Code
- Incident ID
- Assessor info
- All damage assessment details
- All required actions (multi-select)
- All impact estimates

### Non-Editable Fields
- Status (remains DRAFT while editing)
- Created date/time

### Edit Restrictions
- Completed assessments cannot be edited
- Must complete to finalize

### Save Options
- **Update**: Save changes to existing assessment
- **Cancel**: Discard changes without saving

---

## 🎨 UI/UX Features

### Color System
```
Primary: Blue (#2563EB) - Actions, highlights
Success: Green (#10B981) - Completed, positive
Warning: Yellow (#FBBF24) - Draft, caution
Danger: Red (#EF4444) - Critical, casualties
Info: Gray (#6B7280) - Secondary, disabled
```

### Icons (Lucide React)
- Loading spinner for async operations
- Alert icons for status indicators
- Eye icon for viewing details
- Edit/trash for quick actions
- Check for completion
- Upload for photos

### Responsive Design
- Mobile: Single column layouts
- Tablet: 2-column layouts
- Desktop: 3-column layouts
- Touch-friendly button sizes

### Animations & Transitions
- Smooth hover effects on buttons
- Loading spinner animation
- Modal fade-in
- Tab switches
- Hover card elevation

---

## 🔄 State Management

### Zustand Store
Manages global assessment state:

```typescript
assessments[]           // All assessments
selectedAssessment      // Currently viewing
loading boolean         // API request state
error string           // Error messages

Functions:
- setAssessments()     // Update all
- addAssessment()      // Add new
- updateAssessment()   // Modify existing
- removeAssessment()   // Delete
- setLoading()         // Set loading state
- setError()           // Show error
- clearError()         // Clear error
```

### Local State
- Form input values
- Selected tab
- Modal visibility
- Editing state

---

## 🌐 API Integration

### All Endpoints Called

**Create**
```
POST /api/assessments
Body: Complete assessment object
Response: Created assessment with ID
```

**Retrieve**
```
GET /api/assessments              # All
GET /api/assessments/{id}         # Single
GET /api/assessments/incident/{id} # By incident
GET /api/assessments/completed    # Completed only
```

**Update**
```
PUT /api/assessments/{id}
Body: Updated assessment fields
```

**Complete**
```
PUT /api/assessments/{id}/complete
Response: Completed assessment
Event: Published to RabbitMQ
```

**Photos**
```
POST /api/assessments/{id}/photos
Body: FormData with file
Response: Assessment with new photo
```

**Delete**
```
DELETE /api/assessments/{id}
Response: 204 No Content
```

### Authentication
- JWT token from localStorage
- Automatically added to headers
- On every API request

### Error Handling
- HTTP error responses caught
- User-friendly error messages
- Failed requests logged
- Retry capability with refresh

---

## 📱 Responsive Breakpoints

- **Mobile** (< 640px): Single column
- **Tablet** (640px-1024px): Two columns
- **Desktop** (> 1024px): Three columns

Grid cards adjust automatically for each screen size.

---

## 🔐 Security Features

- JWT authentication on all requests
- FormData for file uploads
- Input validation on forms
- CSRF protection via axios
- Secure error messages
- No sensitive data in console logs

---

## ⚡ Performance Optimizations

- Code splitting with Vite
- Lazy component loading
- Efficient state updates
- Minimal re-renders with React
- Image optimization
- CSS minification
- Bundle size: ~258KB (gzipped: ~82KB)

---

## 🚀 Future Enhancement Ideas

1. **Batch Operations**
   - Select multiple assessments
   - Bulk complete/delete
   - Bulk export to CSV

2. **Advanced Filtering**
   - Filter by date range
   - Filter by severity
   - Filter by assessor
   - Search by findings

3. **Export Features**
   - Export to PDF
   - Export to Excel
   - Generate reports

4. **Real-time Updates**
   - WebSocket for live data
   - Auto-refresh assessments
   - Push notifications

5. **Mobile App**
   - React Native version
   - Offline mode
   - Native camera integration

6. **Analytics**
   - Dashboard statistics
   - Charts and graphs
   - Trend analysis
   - Heat maps

7. **Comments & Collaboration**
   - Add comments to assessments
   - @mention other users
   - Activity timeline

8. **Template System**
   - Save assessment templates
   - Quick-fill common fields
   - Assessment standards

---

## 📚 Learn More

- See [README.md](./README.md) for full documentation
- Check [SETUP.md](./SETUP.md) for quick start
- Review backend at [PERSON_7_ASSESSMENT_SERVICE.md](../PERSON_7_ASSESSMENT_SERVICE.md)
