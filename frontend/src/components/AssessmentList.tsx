import { useEffect, useState } from 'react'
import type { Assessment } from '../lib/assessmentStore'
import { useAssessmentStore } from '../lib/assessmentStore'
import { assessmentAPI } from '../lib/api'
import { Loader, AlertCircle, CheckCircle, Edit2, Trash2, Eye } from 'lucide-react'

interface AssessmentListProps {
  onSelectAssessment: (assessment: Assessment) => void
  onEditAssessment: (assessment: Assessment) => void
  filter?: 'all' | 'draft' | 'completed'
}

export function AssessmentList({ onSelectAssessment, onEditAssessment, filter = 'all' }: AssessmentListProps) {
  const { assessments, loading, error, setAssessments, setLoading, setError, removeAssessment } = useAssessmentStore()
  const [filteredAssessments, setFilteredAssessments] = useState<Assessment[]>([])

  useEffect(() => {
    const fetchAssessments = async () => {
      setLoading(true)
      try {
        let response
        if (filter === 'completed') {
          response = await assessmentAPI.getCompletedAssessments()
        } else {
          response = await assessmentAPI.getAllAssessments()
        }
        setAssessments(response.data)
        setError(null)
      } catch (err: any) {
        const errorMsg = err.userMessage || err.message || 'Failed to fetch assessments'
        console.error('Fetch assessments error:', {
          error: err,
          userMessage: errorMsg,
          status: err.response?.status,
          data: err.response?.data
        })
        setError(errorMsg)
      } finally {
        setLoading(false)
      }
    }
    
    fetchAssessments()
  }, [filter, setAssessments, setLoading, setError])

  useEffect(() => {
    let filtered = assessments
    if (filter !== 'all') {
      filtered = assessments.filter(a => a.status === (filter === 'completed' ? 'COMPLETED' : 'DRAFT'))
    }
    setFilteredAssessments(filtered)
  }, [assessments, filter])

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this assessment?')) {
      try {
        await assessmentAPI.deleteAssessment(id)
        removeAssessment(id)
      } catch (err) {
        setError('Failed to delete assessment')
      }
    }
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

  const getStatusIcon = (status: string) => {
    return status === 'COMPLETED' ? (
      <CheckCircle className="w-5 h-5 text-green-600" />
    ) : (
      <AlertCircle className="w-5 h-5 text-yellow-600" />
    )
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    )
  }

  if (error) {
    return (
      <div className="bg-red-50 border border-red-200 rounded-lg p-4">
        <div className="flex items-start justify-between">
          <div className="flex items-start">
            <AlertCircle className="w-5 h-5 mr-2 text-red-700 mt-0.5 flex-shrink-0" />
            <div>
              <p className="text-red-800 font-medium">Connection Error</p>
              <p className="text-red-700 text-sm mt-1">{error}</p>
              <p className="text-red-600 text-xs mt-2">
                💡 Tip: Make sure the backend Assessment Service is running on port 8087
              </p>
            </div>
          </div>
          <button
            onClick={() => window.location.reload()}
            className="px-3 py-1 bg-red-200 hover:bg-red-300 text-red-900 rounded text-sm font-medium transition-colors"
          >
            Retry
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-4">
      {filteredAssessments.length === 0 ? (
        <div className="text-center py-12 bg-gray-50 rounded-lg">
          <p className="text-gray-500 text-lg">No assessments found</p>
        </div>
      ) : (
        <div className="grid gap-4">
          {filteredAssessments.map((assessment) => (
            <div
              key={assessment.id}
              className="bg-white rounded-lg shadow border border-gray-200 hover:shadow-md transition-shadow"
            >
              <div className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      {getStatusIcon(assessment.status)}
                      <h3 className="text-lg font-semibold text-gray-900">
                        {assessment.assessmentCode}
                      </h3>
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${getSeverityColor(assessment.severity)}`}>
                        {assessment.severity}
                      </span>
                    </div>
                    
                    <div className="mt-3 space-y-2">
                      <p className="text-sm text-gray-600">
                        <span className="font-medium">Assessor:</span> {assessment.assessorName}
                      </p>
                      <p className="text-sm text-gray-600">
                        <span className="font-medium">Incident ID:</span> {assessment.incidentId}
                      </p>
                      <p className="text-sm text-gray-600 line-clamp-2">
                        <span className="font-medium">Findings:</span> {assessment.findings}
                      </p>
                      {assessment.requiredActions.length > 0 && (
                        <div className="flex flex-wrap gap-2 mt-2">
                          {assessment.requiredActions.slice(0, 3).map((action) => (
                            <span key={action} className="bg-purple-100 text-purple-700 text-xs px-2 py-1 rounded">
                              {action}
                            </span>
                          ))}
                          {assessment.requiredActions.length > 3 && (
                            <span className="text-xs text-gray-500">
                              +{assessment.requiredActions.length - 3} more
                            </span>
                          )}
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="flex gap-2 ml-4">
                    <button
                      onClick={() => onSelectAssessment(assessment)}
                      className="p-2 hover:bg-blue-50 rounded-lg text-blue-600 transition-colors"
                      title="View details"
                    >
                      <Eye className="w-5 h-5" />
                    </button>
                    {assessment.status === 'DRAFT' && (
                      <button
                        onClick={() => onEditAssessment(assessment)}
                        className="p-2 hover:bg-green-50 rounded-lg text-green-600 transition-colors"
                        title="Edit"
                      >
                        <Edit2 className="w-5 h-5" />
                      </button>
                    )}
                    <button
                      onClick={() => handleDelete(assessment.id!)}
                      className="p-2 hover:bg-red-50 rounded-lg text-red-600 transition-colors"
                      title="Delete"
                    >
                      <Trash2 className="w-5 h-5" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
