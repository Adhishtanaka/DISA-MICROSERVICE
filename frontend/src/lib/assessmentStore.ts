import { create } from 'zustand'

export interface AssessmentPhoto {
  url: string
  name: string
}

export interface Assessment {
  id?: number
  assessmentCode: string
  incidentId: number
  assessorId: number
  assessorName: string
  severity: 'MINOR' | 'MODERATE' | 'SEVERE' | 'CRITICAL'
  findings: string
  recommendations: string
  requiredActions: string[]
  photoUrls: string[]
  status: 'DRAFT' | 'COMPLETED'
  estimatedCasualties: number
  estimatedDisplaced: number
  affectedInfrastructure: string
  createdAt?: string
  completedAt?: string
}

interface AssessmentStore {
  assessments: Assessment[]
  selectedAssessment: Assessment | null
  loading: boolean
  error: string | null
  
  setAssessments: (assessments: Assessment[]) => void
  setSelectedAssessment: (assessment: Assessment | null) => void
  addAssessment: (assessment: Assessment) => void
  updateAssessment: (assessment: Assessment) => void
  removeAssessment: (id: number) => void
  setLoading: (loading: boolean) => void
  setError: (error: string | null) => void
  clearError: () => void
}

export const useAssessmentStore = create<AssessmentStore>((set) => ({
  assessments: [],
  selectedAssessment: null,
  loading: false,
  error: null,
  
  setAssessments: (assessments) => set({ assessments }),
  setSelectedAssessment: (selectedAssessment) => set({ selectedAssessment }),
  addAssessment: (assessment) => 
    set((state) => ({
      assessments: [...state.assessments, assessment]
    })),
  updateAssessment: (assessment) =>
    set((state) => ({
      assessments: state.assessments.map((a) => 
        a.id === assessment.id ? assessment : a
      )
    })),
  removeAssessment: (id) =>
    set((state) => ({
      assessments: state.assessments.filter((a) => a.id !== id)
    })),
  setLoading: (loading) => set({ loading }),
  setError: (error) => set({ error }),
  clearError: () => set({ error: null })
}))
