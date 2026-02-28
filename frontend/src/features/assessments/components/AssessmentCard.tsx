import { Assessment } from "../types/assessment.types";

interface AssessmentCardProps {
  assessment: Assessment;
  onComplete?: (id: number) => void;
  onDelete?: (id: number) => void;
}

export default function AssessmentCard({ assessment, onComplete, onDelete }: AssessmentCardProps) {
  const severityColors = {
    MINOR: "bg-blue-100 text-blue-800",
    MODERATE: "bg-yellow-100 text-yellow-800",
    SEVERE: "bg-orange-100 text-orange-800",
    CRITICAL: "bg-red-100 text-red-800",
  };

  const statusColors = {
    DRAFT: "bg-gray-100 text-gray-800",
    COMPLETED: "bg-green-100 text-green-800",
  };

  return (
    <div className="border rounded-lg p-4 shadow-sm bg-white hover:shadow-md transition-shadow">
      <div className="flex justify-between items-start mb-3">
        <div>
          <h3 className="font-semibold text-lg">{assessment.assessmentCode}</h3>
          <p className="text-sm text-gray-600">by {assessment.assessorName}</p>
        </div>
        <div className="flex gap-2">
          <span className={`px-2 py-1 rounded text-xs font-medium ${severityColors[assessment.severity]}`}>
            {assessment.severity}
          </span>
          <span className={`px-2 py-1 rounded text-xs font-medium ${statusColors[assessment.status]}`}>
            {assessment.status}
          </span>
        </div>
      </div>

      <div className="space-y-2 mb-3">
        <div>
          <p className="text-sm font-medium text-gray-700">Findings:</p>
          <p className="text-sm text-gray-600">{assessment.findings}</p>
        </div>

        {assessment.recommendations && (
          <div>
            <p className="text-sm font-medium text-gray-700">Recommendations:</p>
            <p className="text-sm text-gray-600">{assessment.recommendations}</p>
          </div>
        )}

        {assessment.requiredActions && assessment.requiredActions.length > 0 && (
          <div>
            <p className="text-sm font-medium text-gray-700">Required Actions:</p>
            <div className="flex flex-wrap gap-1 mt-1">
              {assessment.requiredActions.map((action, idx) => (
                <span key={idx} className="px-2 py-1 bg-purple-100 text-purple-800 rounded text-xs">
                  {action}
                </span>
              ))}
            </div>
          </div>
        )}

        <div className="grid grid-cols-2 gap-2 text-sm">
          {assessment.estimatedCasualties !== null && (
            <div>
              <span className="font-medium text-gray-700">Casualties:</span>
              <span className="ml-1 text-gray-600">{assessment.estimatedCasualties}</span>
            </div>
          )}
          {assessment.estimatedDisplaced !== null && (
            <div>
              <span className="font-medium text-gray-700">Displaced:</span>
              <span className="ml-1 text-gray-600">{assessment.estimatedDisplaced}</span>
            </div>
          )}
        </div>

        {assessment.affectedInfrastructure && (
          <div>
            <p className="text-sm font-medium text-gray-700">Affected Infrastructure:</p>
            <p className="text-sm text-gray-600">{assessment.affectedInfrastructure}</p>
          </div>
        )}

        {assessment.photoUrls && assessment.photoUrls.length > 0 && (
          <div>
            <p className="text-sm font-medium text-gray-700">Photos: {assessment.photoUrls.length}</p>
          </div>
        )}
      </div>

      <div className="flex justify-between items-center text-xs text-gray-500 border-t pt-2">
        <span>Created: {new Date(assessment.createdAt).toLocaleString()}</span>
        {assessment.completedAt && (
          <span>Completed: {new Date(assessment.completedAt).toLocaleString()}</span>
        )}
      </div>

      {(onComplete || onDelete) && (
        <div className="flex gap-2 mt-3">
          {onComplete && assessment.status === "DRAFT" && (
            <button
              onClick={() => onComplete(assessment.id)}
              className="px-3 py-1 bg-green-600 text-white rounded text-sm hover:bg-green-700"
            >
              Complete Assessment
            </button>
          )}
          {onDelete && (
            <button
              onClick={() => onDelete(assessment.id)}
              className="px-3 py-1 bg-red-600 text-white rounded text-sm hover:bg-red-700"
            >
              Delete
            </button>
          )}
        </div>
      )}
    </div>
  );
}
