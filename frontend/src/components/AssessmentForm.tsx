import { useState } from 'react'
import type { Assessment } from '../lib/assessmentStore'
import { useAssessmentStore } from '../lib/assessmentStore'
import { assessmentAPI } from '../lib/api'
import { X, AlertCircle } from 'lucide-react'

interface AssessmentFormProps {
  assessment?: Assessment
  onClose: () => void
  onSuccess: () => void
}

const DAMAGE_SEVERITIES = ['MINOR', 'MODERATE', 'SEVERE', 'CRITICAL']
const REQUIRED_ACTIONS = [
  'RESCUE',
  'MEDICAL_AID',
  'DEBRIS_REMOVAL',
  'STRUCTURAL_REPAIR',
  'UTILITY_RESTORATION',
  'EVACUATION',
  'SHELTER_PROVISION',
  'SANITATION'
]

export function AssessmentForm({ assessment, onClose, onSuccess }: AssessmentFormProps) {
  const { addAssessment, updateAssessment, setError } = useAssessmentStore()
  const [loading, setLoading] = useState(false)
  const [localError, setLocalError] = useState<string | null>(null)
  const [formData, setFormData] = useState<Partial<Assessment>>(
    assessment || {
      assessmentCode: '',
      incidentId: 0,
      assessorId: 0,
      assessorName: '',
      severity: 'MODERATE',
      findings: '',
      recommendations: '',
      requiredActions: [],
      photoUrls: [],
      estimatedCasualties: 0,
      estimatedDisplaced: 0,
      affectedInfrastructure: '',
      status: 'DRAFT'
    }
  )
  const [selectedActions, setSelectedActions] = useState<string[]>(
    assessment?.requiredActions || []
  )

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: name.includes('Id') || name.includes('Estimated') ? parseInt(value) || 0 : value
    }))
  }

  const toggleAction = (action: string) => {
    setSelectedActions(prev =>
      prev.includes(action)
        ? prev.filter(a => a !== action)
        : [...prev, action]
    )
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setLocalError(null)

    try {
      const submitData = {
        ...formData,
        requiredActions: selectedActions
      }

      if (assessment?.id) {
        const response = await assessmentAPI.updateAssessment(assessment.id, submitData)
        updateAssessment(response.data)
        console.log('✅ Assessment updated:', response.data)
      } else {
        const response = await assessmentAPI.createAssessment(submitData)
        addAssessment(response.data)
        console.log('✅ Assessment created:', response.data)
      }
      
      onSuccess()
      onClose()
    } catch (err: any) {
      const errorMsg = err.userMessage || err.response?.data?.message || 'Failed to save assessment'
      console.error('❌ Form submission error:', {
        message: errorMsg,
        error: err,
        status: err.response?.status
      })
      setLocalError(errorMsg)
      setError(errorMsg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex justify-between items-center">
          <h2 className="text-2xl font-bold text-gray-900">
            {assessment ? 'Edit Assessment' : 'Create New Assessment'}
          </h2>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <X className="w-6 h-6" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {/* Error Alert */}
          {localError && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4">
              <div className="flex items-start gap-3">
                <AlertCircle className="w-5 h-5 text-red-600 mt-0.5 flex-shrink-0" />
                <div>
                  <p className="text-red-800 font-medium">Error</p>
                  <p className="text-red-700 text-sm mt-1">{localError}</p>
                </div>
                <button
                  type="button"
                  onClick={() => setLocalError(null)}
                  className="text-red-600 hover:text-red-800"
                >
                  ✕
                </button>
              </div>
            </div>
          )}
          
          {/* Basic Info */}
          <div className="space-y-4">
            <h3 className="text-lg font-semibold text-gray-900">Basic Information</h3>
            
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Assessment Code
                </label>
                <input
                  type="text"
                  name="assessmentCode"
                  value={formData.assessmentCode}
                  onChange={handleInputChange}
                  placeholder="e.g., ASS-001"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Incident ID
                </label>
                <input
                  type="number"
                  name="incidentId"
                  value={formData.incidentId}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Assessor ID
                </label>
                <input
                  type="number"
                  name="assessorId"
                  value={formData.assessorId}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Assessor Name
                </label>
                <input
                  type="text"
                  name="assessorName"
                  value={formData.assessorName}
                  onChange={handleInputChange}
                  placeholder="e.g., John Doe"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>
          </div>

          {/* Damage Assessment */}
          <div className="space-y-4">
            <h3 className="text-lg font-semibold text-gray-900">Damage Assessment</h3>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Severity Level
              </label>
              <select
                name="severity"
                value={formData.severity}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                {DAMAGE_SEVERITIES.map(severity => (
                  <option key={severity} value={severity}>
                    {severity}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Findings
              </label>
              <textarea
                name="findings"
                value={formData.findings}
                onChange={handleInputChange}
                placeholder="Describe the damage findings..."
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Recommendations
              </label>
              <textarea
                name="recommendations"
                value={formData.recommendations}
                onChange={handleInputChange}
                placeholder="Provide recommendations..."
                rows={2}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Affected Infrastructure
              </label>
              <input
                type="text"
                name="affectedInfrastructure"
                value={formData.affectedInfrastructure}
                onChange={handleInputChange}
                placeholder="e.g., Buildings, Roads, Utilities"
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          {/* Required Actions */}
          <div className="space-y-4">
            <h3 className="text-lg font-semibold text-gray-900">Required Actions</h3>
            <div className="grid grid-cols-2 gap-3">
              {REQUIRED_ACTIONS.map(action => (
                <label key={action} className="flex items-center">
                  <input
                    type="checkbox"
                    checked={selectedActions.includes(action)}
                    onChange={() => toggleAction(action)}
                    className="w-4 h-4 text-blue-600 rounded border-gray-300 focus:ring-2 focus:ring-blue-500"
                  />
                  <span className="ml-2 text-sm text-gray-700">{action}</span>
                </label>
              ))}
            </div>
          </div>

          {/* Impact Estimates */}
          <div className="space-y-4">
            <h3 className="text-lg font-semibold text-gray-900">Impact Estimates</h3>
            
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Estimated Casualties
                </label>
                <input
                  type="number"
                  name="estimatedCasualties"
                  value={formData.estimatedCasualties}
                  onChange={handleInputChange}
                  min={0}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Estimated Displaced
                </label>
                <input
                  type="number"
                  name="estimatedDisplaced"
                  value={formData.estimatedDisplaced}
                  onChange={handleInputChange}
                  min={0}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>
          </div>

          {/* Actions */}
          <div className="flex gap-3 pt-4 border-t border-gray-200">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-4 py-2 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors disabled:opacity-50"
            >
              {loading ? 'Saving...' : assessment ? 'Update' : 'Create'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
