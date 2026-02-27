import { useState } from 'react'
import type { Assessment } from '../lib/assessmentStore'
import { assessmentAPI } from '../lib/api'
import { X, CheckCircle, AlertCircle, Loader } from 'lucide-react'
import { PhotoUpload } from './PhotoUpload'

interface AssessmentDetailsProps {
  assessment: Assessment
  onClose: () => void
  onCompleted?: () => void
}

export function AssessmentDetails({ assessment, onClose, onCompleted }: AssessmentDetailsProps) {
  const [loading, setLoading] = useState(false)
  const [photos] = useState<string[]>(assessment.photoUrls || [])
  const [activeTab, setActiveTab] = useState<'overview' | 'photos' | 'actions'>('overview')

  const handleCompleteAssessment = async () => {
    if (!window.confirm('Are you sure you want to complete this assessment? This action cannot be undone.')) {
      return
    }

    setLoading(true)
    try {
      await assessmentAPI.completeAssessment(assessment.id!)
      onCompleted?.()
    } catch (err) {
      console.error('Failed to complete assessment')
    } finally {
      setLoading(false)
    }
  }

  const handlePhotoAdded = () => {
    // In a real scenario, you would fetch the updated assessment or handle this differently
    onCompleted?.()
  }

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case 'MINOR':
        return 'bg-blue-100 text-blue-800'
      case 'MODERATE':
        return 'bg-yellow-100 text-yellow-800'
      case 'SEVERE':
        return 'bg-orange-100 text-orange-800'
      case 'CRITICAL':
        return 'bg-red-100 text-red-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-hidden flex flex-col">
        {/* Header */}
        <div className="sticky top-0 bg-gradient-to-r from-blue-600 to-blue-700 text-white px-6 py-4 flex justify-between items-start">
          <div>
            <div className="flex items-center gap-3 mb-2">
              {assessment.status === 'COMPLETED' ? (
                <CheckCircle className="w-6 h-6" />
              ) : (
                <AlertCircle className="w-6 h-6" />
              )}
              <h2 className="text-2xl font-bold">{assessment.assessmentCode}</h2>
            </div>
            <p className="text-blue-100">
              {assessment.status === 'COMPLETED' ? 'Completed' : 'Draft'} Assessment
            </p>
          </div>
          <button
            onClick={onClose}
            className="p-2 hover:bg-blue-500 rounded-lg transition-colors"
          >
            <X className="w-6 h-6" />
          </button>
        </div>

        {/* Tabs */}
        <div className="border-b border-gray-200 bg-gray-50 flex">
          {(['overview', 'photos', 'actions'] as const).map(tab => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-6 py-3 font-medium transition-colors capitalize ${
                activeTab === tab
                  ? 'border-b-2 border-blue-600 text-blue-600'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              {tab}
            </button>
          ))}
        </div>

        {/* Content */}
        <div className="overflow-y-auto flex-1">
          {activeTab === 'overview' && (
            <div className="p-6 space-y-6">
              {/* Status & Severity */}
              <div className="grid grid-cols-2 gap-4">
                <div className="bg-gray-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600 font-medium">Status</p>
                  <p className="text-lg font-semibold text-gray-900 mt-1">
                    {assessment.status}
                  </p>
                </div>
                <div className="bg-gray-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600 font-medium">Severity</p>
                  <span className={`inline-block mt-1 px-3 py-1 rounded-full text-sm font-medium ${getSeverityColor(assessment.severity)}`}>
                    {assessment.severity}
                  </span>
                </div>
              </div>

              {/* Assessor Info */}
              <div className="grid grid-cols-3 gap-4">
                <div>
                  <p className="text-sm text-gray-600 font-medium">Assessor</p>
                  <p className="text-lg font-semibold text-gray-900 mt-1">{assessment.assessorName}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 font-medium">Assessor ID</p>
                  <p className="text-lg font-semibold text-gray-900 mt-1">{assessment.assessorId}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 font-medium">Incident ID</p>
                  <p className="text-lg font-semibold text-gray-900 mt-1">{assessment.incidentId}</p>
                </div>
              </div>

              {/* Findings */}
              <div>
                <h3 className="text-sm font-semibold text-gray-900 mb-2">Findings</h3>
                <p className="text-gray-700 leading-relaxed">{assessment.findings}</p>
              </div>

              {/* Recommendations */}
              <div>
                <h3 className="text-sm font-semibold text-gray-900 mb-2">Recommendations</h3>
                <p className="text-gray-700 leading-relaxed">{assessment.recommendations}</p>
              </div>

              {/* Affected Infrastructure */}
              <div>
                <h3 className="text-sm font-semibold text-gray-900 mb-2">Affected Infrastructure</h3>
                <p className="text-gray-700">{assessment.affectedInfrastructure}</p>
              </div>

              {/* Impact */}
              <div className="grid grid-cols-2 gap-4 bg-red-50 rounded-lg p-4">
                <div>
                  <p className="text-sm text-red-600 font-medium">Estimated Casualties</p>
                  <p className="text-2xl font-bold text-red-700 mt-1">{assessment.estimatedCasualties}</p>
                </div>
                <div>
                  <p className="text-sm text-red-600 font-medium">Estimated Displaced</p>
                  <p className="text-2xl font-bold text-red-700 mt-1">{assessment.estimatedDisplaced}</p>
                </div>
              </div>
            </div>
          )}

          {activeTab === 'photos' && (
            <div className="p-6 space-y-6">
              {assessment.status === 'DRAFT' && (
                <PhotoUpload
                  assessmentId={assessment.id!}
                  photos={photos}
                  onPhotoAdded={handlePhotoAdded}
                />
              )}

              {photos.length > 0 && (
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">Assessment Photos</h3>
                  <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
                    {photos.map((photo, index) => (
                      <div key={index} className="bg-gray-100 rounded-lg aspect-square overflow-hidden">
                        <img
                          src={photo}
                          alt={`Assessment photo ${index + 1}`}
                          className="w-full h-full object-cover"
                        />
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {photos.length === 0 && assessment.status === 'DRAFT' && (
                <div className="text-center py-12 bg-gray-50 rounded-lg">
                  <p className="text-gray-500">No photos uploaded yet</p>
                </div>
              )}
            </div>
          )}

          {activeTab === 'actions' && (
            <div className="p-6 space-y-4">
              <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                <h3 className="font-semibold text-blue-900 mb-3">Required Actions</h3>
                {assessment.requiredActions && assessment.requiredActions.length > 0 ? (
                  <div className="space-y-2">
                    {assessment.requiredActions.map((action) => (
                      <div key={action} className="flex items-center">
                        <div className="w-2 h-2 bg-blue-600 rounded-full mr-3"></div>
                        <span className="text-blue-900">{action}</span>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-blue-700">No required actions specified</p>
                )}
              </div>

              {assessment.status === 'DRAFT' && (
                <button
                  onClick={handleCompleteAssessment}
                  disabled={loading}
                  className="w-full mt-4 px-4 py-3 bg-green-600 text-white rounded-lg font-semibold hover:bg-green-700 transition-colors disabled:opacity-50 flex items-center justify-center gap-2"
                >
                  {loading ? (
                    <>
                      <Loader className="w-5 h-5 animate-spin" />
                      Completing...
                    </>
                  ) : (
                    <>
                      <CheckCircle className="w-5 h-5" />
                      Complete Assessment
                    </>
                  )}
                </button>
              )}

              {assessment.status === 'COMPLETED' && (
                <div className="bg-green-50 border border-green-200 rounded-lg p-4 text-center">
                  <CheckCircle className="w-8 h-8 text-green-600 mx-auto mb-2" />
                  <p className="text-green-900 font-medium">Assessment Completed</p>
                  <p className="text-green-700 text-sm mt-1">
                    Completed on {assessment.completedAt ? new Date(assessment.completedAt).toLocaleDateString() : 'Unknown'}
                  </p>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
