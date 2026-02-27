import { useState } from 'react'
import type { Assessment } from '../lib/assessmentStore'
import { AssessmentList } from './AssessmentList'
import { AssessmentForm } from './AssessmentForm'
import { AssessmentDetails } from './AssessmentDetails'
import { Plus, BarChart3, CheckCircle, AlertCircle } from 'lucide-react'

export function Dashboard() {
  const [showForm, setShowForm] = useState(false)
  const [selectedAssessment, setSelectedAssessment] = useState<Assessment | null>(null)
  const [editingAssessment, setEditingAssessment] = useState<Assessment | null>(null)
  const [activeTab, setActiveTab] = useState<'all' | 'draft' | 'completed'>('all')

  const handleSelectAssessment = (assessment: Assessment) => {
    setSelectedAssessment(assessment)
    setEditingAssessment(null)
  }

  const handleEditAssessment = (assessment: Assessment) => {
    setEditingAssessment(assessment)
    setSelectedAssessment(null)
  }

  const handleFormClose = () => {
    setShowForm(false)
    setEditingAssessment(null)
  }

  const handleFormSuccess = () => {
    setShowForm(false)
    setEditingAssessment(null)
  }

  const handleDetailsClose = () => {
    setSelectedAssessment(null)
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      {/* Header */}
      <div className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-6 py-6">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-2">
                <BarChart3 className="w-8 h-8 text-blue-600" />
                Assessment Service
              </h1>
              <p className="text-gray-500 text-sm mt-1">Manage damage assessments and generate follow-up tasks</p>
            </div>
            <button
              onClick={() => setShowForm(true)}
              className="flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 transition-colors shadow-lg hover:shadow-xl"
            >
              <Plus className="w-5 h-5" />
              New Assessment
            </button>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto px-6 py-8">
        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          <div className="bg-white rounded-lg shadow p-6 border-l-4 border-blue-600">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-500 text-sm font-medium">All Assessments</p>
                <p className="text-3xl font-bold text-gray-900 mt-1">-</p>
              </div>
              <BarChart3 className="w-12 h-12 text-blue-100" />
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6 border-l-4 border-yellow-600">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-500 text-sm font-medium">Draft Assessments</p>
                <p className="text-3xl font-bold text-gray-900 mt-1">-</p>
              </div>
              <AlertCircle className="w-12 h-12 text-yellow-100" />
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6 border-l-4 border-green-600">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-500 text-sm font-medium">Completed Assessments</p>
                <p className="text-3xl font-bold text-gray-900 mt-1">-</p>
              </div>
              <CheckCircle className="w-12 h-12 text-green-100" />
            </div>
          </div>
        </div>

        {/* Tabs */}
        <div className="bg-white rounded-lg shadow mb-8">
          <div className="border-b border-gray-200 flex">
            {(['all', 'draft', 'completed'] as const).map(tab => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`flex-1 px-6 py-4 font-semibold transition-all capitalize ${
                  activeTab === tab
                    ? 'border-b-2 border-blue-600 text-blue-600'
                    : 'text-gray-600 hover:text-gray-900'
                }`}
              >
                {tab === 'all' && 'All Assessments'}
                {tab === 'draft' && 'Draft'}
                {tab === 'completed' && 'Completed'}
              </button>
            ))}
          </div>

          <div className="p-6">
            <AssessmentList
              filter={activeTab}
              onSelectAssessment={handleSelectAssessment}
              onEditAssessment={handleEditAssessment}
            />
          </div>
        </div>
      </div>

      {/* Modals */}
      {showForm && (
        <AssessmentForm
          assessment={editingAssessment || undefined}
          onClose={handleFormClose}
          onSuccess={handleFormSuccess}
        />
      )}

      {selectedAssessment && !showForm && (
        <AssessmentDetails
          assessment={selectedAssessment}
          onClose={handleDetailsClose}
          onCompleted={handleDetailsClose}
        />
      )}

      {editingAssessment && !selectedAssessment && (
        <AssessmentForm
          assessment={editingAssessment}
          onClose={handleFormClose}
          onSuccess={handleFormSuccess}
        />
      )}
    </div>
  )
}
