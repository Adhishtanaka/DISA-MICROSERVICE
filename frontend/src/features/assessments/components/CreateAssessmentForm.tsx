import { useState } from "react";
import { CreateAssessmentRequest, DamageSeverity } from "../types/assessment.types";

interface CreateAssessmentFormProps {
  onSubmit: (data: CreateAssessmentRequest) => void;
  onCancel: () => void;
}

export default function CreateAssessmentForm({ onSubmit, onCancel }: CreateAssessmentFormProps) {
  const [formData, setFormData] = useState<CreateAssessmentRequest>({
    incidentId: 1,
    assessorId: 301,
    assessorName: "",
    severity: "MODERATE",
    findings: "",
    recommendations: "",
    requiredActions: [],
    estimatedCasualties: 0,
    estimatedDisplaced: 0,
    affectedInfrastructure: "",
    status: "DRAFT",
  });

  const [actionInput, setActionInput] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const addAction = () => {
    if (actionInput.trim()) {
      setFormData({
        ...formData,
        requiredActions: [...(formData.requiredActions || []), actionInput.trim()],
      });
      setActionInput("");
    }
  };

  const removeAction = (index: number) => {
    setFormData({
      ...formData,
      requiredActions: formData.requiredActions?.filter((_, i) => i !== index) || [],
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 bg-white p-6 rounded-lg shadow">
      <h2 className="text-xl font-semibold mb-4">Create New Assessment</h2>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium mb-1">Incident ID</label>
          <input
            type="number"
            required
            value={formData.incidentId}
            onChange={(e) => setFormData({ ...formData, incidentId: Number(e.target.value) })}
            className="w-full border rounded px-3 py-2"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Assessor ID</label>
          <input
            type="number"
            required
            value={formData.assessorId}
            onChange={(e) => setFormData({ ...formData, assessorId: Number(e.target.value) })}
            className="w-full border rounded px-3 py-2"
          />
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Assessor Name</label>
        <input
          type="text"
          required
          value={formData.assessorName}
          onChange={(e) => setFormData({ ...formData, assessorName: e.target.value })}
          className="w-full border rounded px-3 py-2"
        />
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Severity</label>
        <select
          value={formData.severity}
          onChange={(e) => setFormData({ ...formData, severity: e.target.value as DamageSeverity })}
          className="w-full border rounded px-3 py-2"
        >
          <option value="MINOR">Minor</option>
          <option value="MODERATE">Moderate</option>
          <option value="SEVERE">Severe</option>
          <option value="CRITICAL">Critical</option>
        </select>
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Findings</label>
        <textarea
          required
          value={formData.findings}
          onChange={(e) => setFormData({ ...formData, findings: e.target.value })}
          className="w-full border rounded px-3 py-2"
          rows={3}
        />
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Recommendations</label>
        <textarea
          value={formData.recommendations}
          onChange={(e) => setFormData({ ...formData, recommendations: e.target.value })}
          className="w-full border rounded px-3 py-2"
          rows={2}
        />
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Required Actions</label>
        <div className="flex gap-2 mb-2">
          <input
            type="text"
            value={actionInput}
            onChange={(e) => setActionInput(e.target.value)}
            onKeyPress={(e) => e.key === "Enter" && (e.preventDefault(), addAction())}
            placeholder="e.g., RESCUE, MEDICAL_AID"
            className="flex-1 border rounded px-3 py-2"
          />
          <button
            type="button"
            onClick={addAction}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            Add
          </button>
        </div>
        <div className="flex flex-wrap gap-2">
          {formData.requiredActions?.map((action, idx) => (
            <span key={idx} className="px-2 py-1 bg-purple-100 text-purple-800 rounded text-sm flex items-center gap-1">
              {action}
              <button type="button" onClick={() => removeAction(idx)} className="text-purple-600 hover:text-purple-800">
                ×
              </button>
            </span>
          ))}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium mb-1">Estimated Casualties</label>
          <input
            type="number"
            value={formData.estimatedCasualties}
            onChange={(e) => setFormData({ ...formData, estimatedCasualties: Number(e.target.value) })}
            className="w-full border rounded px-3 py-2"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Estimated Displaced</label>
          <input
            type="number"
            value={formData.estimatedDisplaced}
            onChange={(e) => setFormData({ ...formData, estimatedDisplaced: Number(e.target.value) })}
            className="w-full border rounded px-3 py-2"
          />
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Affected Infrastructure</label>
        <input
          type="text"
          value={formData.affectedInfrastructure}
          onChange={(e) => setFormData({ ...formData, affectedInfrastructure: e.target.value })}
          className="w-full border rounded px-3 py-2"
        />
      </div>

      <div className="flex gap-2 justify-end">
        <button
          type="button"
          onClick={onCancel}
          className="px-4 py-2 border rounded hover:bg-gray-100"
        >
          Cancel
        </button>
        <button
          type="submit"
          className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
        >
          Create Assessment
        </button>
      </div>
    </form>
  );
}
