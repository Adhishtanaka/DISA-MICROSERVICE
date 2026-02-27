# Assessment Service Frontend

A modern React + TypeScript frontend for the Disaster Response Assessment Service. This application allows field assessors to create damage assessments, upload photos, and track disaster response tasks.

## Features

- ✨ **Create & Manage Assessments** - Create new damage assessments with detailed information
- 📸 **Photo Upload** - Upload and manage assessment photos with drag-and-drop support
- 🎯 **Severity Tracking** - Track damage severity levels (Minor, Moderate, Severe, Critical)
- 📋 **Required Actions** - Manage and track required response actions
- 🔍 **Search & Filter** - Filter assessments by status (Draft, Completed)
- 📊 **Dashboard** - Clean, intuitive dashboard for assessment management
- 🚀 **Real-time Updates** - Seamless integration with backend API
- 🎨 **Modern UI** - Built with Tailwind CSS for a responsive, modern interface

## Tech Stack

- **React 19** - UI library
- **TypeScript** - Type safety
- **Vite** - Fast build tool
- **Tailwind CSS** - Styling
- **Zustand** - State management
- **Axios** - HTTP client
- **Lucide React** - Icons
- **React Router** - Navigation

## Prerequisites

- Node.js 18+ and npm/pnpm
- Backend Assessment Service running on `http://localhost:8087`

## Installation

1. **Install dependencies:**
   ```bash
   pnpm install
   # or
   npm install
   ```

2. **Configure environment variables:**
   ```bash
   # Copy the example file
   cp .env.example .env
   
   # Update .env with your backend URL
   VITE_API_URL=http://localhost:8087
   ```

3. **Start the development server:**
   ```bash
   pnpm dev
   # or
   npm run dev
   ```

   The app will be available at `http://localhost:5173`

## Build

```bash
# Production build
pnpm build

# Preview production build locally
pnpm preview
```

## Project Structure

```
src/
├── components/
│   ├── Dashboard.tsx           # Main dashboard page
│   ├── AssessmentList.tsx      # Assessment list with filtering
│   ├── AssessmentForm.tsx      # Create/edit assessment form
│   ├── AssessmentDetails.tsx   # View assessment details
│   └── PhotoUpload.tsx         # Photo upload component
├── lib/
│   ├── api.ts                  # API client and endpoints
│   └── assessmentStore.ts      # Zustand store for state management
├── App.tsx                     # Main app component
├── main.tsx                    # Entry point
├── index.css                   # Global styles
├── .env                        # Environment variables
└── .env.example                # Environment variables template
```

## API Integration

The frontend integrates with the Assessment Service backend API:

### Endpoints Used

- `POST /api/assessments` - Create assessment
- `GET /api/assessments` - Get all assessments
- `GET /api/assessments/{id}` - Get single assessment
- `GET /api/assessments/incident/{incidentId}` - Get assessments by incident
- `GET /api/assessments/completed` - Get completed assessments
- `PUT /api/assessments/{id}` - Update assessment
- `PUT /api/assessments/{id}/complete` - Complete assessment
- `POST /api/assessments/{id}/photos` - Upload photo
- `DELETE /api/assessments/{id}` - Delete assessment

### Authentication

JWT tokens are automatically included in all requests from `localStorage`:

```typescript
const token = localStorage.getItem('jwt')
if (token) {
  config.headers['Authorization'] = `Bearer ${token}`
}
```

## Usage Guide

### Creating an Assessment

1. Click "New Assessment" button in the header
2. Fill in the assessment details:
   - Assessment Code
   - Incident & Assessor information
   - Damage severity level
   - Findings and recommendations
   - Required actions
   - Impact estimates
3. Click "Create" to save

### Managing Photos

1. Open an assessment in Draft status
2. Click the "Photos" tab
3. Drag and drop photos or click "Select File"
4. Upload images up to 10MB in size
5. Photos are stored with the assessment

### Completing an Assessment

1. Open an assessment in Draft status
2. Click the "Actions" tab
3. Review required actions
4. Click "Complete Assessment"
5. This publishes an event to trigger task creation

### Viewing Assessment Details

- Click the **eye icon** to view full assessment details
- Click the **edit icon** to modify draft assessments
- Click the **trash icon** to delete assessments

## Component Documentation

### Dashboard
Main container component that manages the overall layout and modal states.

**Props:** None

### AssessmentList
Displays assessments with filtering capabilities.

**Props:**
- `onSelectAssessment` - Callback when viewing assessment details
- `onEditAssessment` - Callback when editing assessment
- `filter` - Filter type ('all' | 'draft' | 'completed')

### AssessmentForm
Modal form for creating and editing assessments.

**Props:**
- `assessment` - Optional assessment to edit
- `onClose` - Callback when closing form
- `onSuccess` - Callback on successful submit

### AssessmentDetails
Modal showing full assessment details with tabs for overview, photos, and actions.

**Props:**
- `assessment` - Assessment to display
- `onClose` - Callback when closing modal
- `onCompleted` - Callback when assessment is completed

### PhotoUpload
Component for uploading photos with drag-and-drop support.

**Props:**
- `assessmentId` - ID of assessment to upload to
- `photos` - Array of photo URLs
- `onPhotoAdded` - Callback when photo is uploaded

## State Management (Zustand)

The store manages assessment state:

```typescript
const { 
  assessments,           // Array of all assessments
  selectedAssessment,    // Currently selected assessment
  loading,              // Loading state
  error,                // Error message
  setAssessments,       // Set all assessments
  setSelectedAssessment,// Select an assessment
  addAssessment,        // Add new assessment
  updateAssessment,     // Update existing assessment
  removeAssessment,     // Remove assessment
  setLoading,           // Set loading state
  setError,             // Set error message
  clearError            // Clear error
} = useAssessmentStore()
```

## Error Handling

The application includes comprehensive error handling:

- API errors are caught and displayed to users
- Network errors show informative messages
- Form validation prevents invalid submissions
- File upload validation checks file type and size

## Styling

The application uses Tailwind CSS v4 for styling:

- Responsive grid layouts
- Custom color schemes for severity levels
- Smooth transitions and hover effects
- Accessible component design
- Dark mode compatible (can be added)

## Development Tips

1. **Debug API calls:** Check browser DevTools Network tab
2. **Check store state:** Use Zustand's built-in debugging
3. **View component props:** Use React DevTools browser extension
4. **Test file uploads:** Use small test images first
5. **Check console for errors:** Provides detailed error messages

## Build & Deploy

### Development
```bash
pnpm dev
```

### Production
```bash
# Build
pnpm build

# Output is in dist/

# Deploy dist/ folder to your hosting service
# Examples: Vercel, Netlify, AWS S3, Azure Static Web Apps
```

### Docker (Optional)
```dockerfile
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## Performance Optimization

- Code splitting enabled in Vite
- Lazy loading for modals
- Efficient state updates with Zustand
- Image optimization for uploads
- Minimal bundle size with tree-shaking

## Browser Support

- Chrome/Chromium 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Troubleshooting

### Backend Connection Issues

**Problem:** "Failed to fetch assessments"

**Solution:** Check that:
- Backend is running on port 8087
- `VITE_API_URL` in `.env` is correct
- CORS is enabled on backend
- No firewall blocking the connection

### File Upload Issues

**Problem:** "File size must be less than 10MB"

**Solution:** Use smaller image files or compress them first

### State Not Updating

**Problem:** Changes don't appear in UI

**Solution:** 
- Check that async operations complete
- Clear localStorage if needed
- Refresh page and try again

## API Documentation

For detailed Assessment Service API documentation, see:
[PERSON_7_ASSESSMENT_SERVICE.md](../../../PERSON_7_ASSESSMENT_SERVICE.md)

## Contributing

When adding new features:

1. Create component files in `src/components/`
2. Update the store in `src/lib/assessmentStore.ts` if needed
3. Follow existing code style and naming conventions
4. Use TypeScript for type safety
5. Test with backend before committing

## License

This project is part of the Disaster Response Microservice System.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review API documentation
3. Check backend service logs
4. Report issues with detailed error messages
