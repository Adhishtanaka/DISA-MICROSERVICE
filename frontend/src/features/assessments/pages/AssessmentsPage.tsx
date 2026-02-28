import { useState } from "react";
import { useAssessments } from "../hooks/useAssessments";
import { assessmentApi } from "../api/assessmentApi";
import AssessmentCard from "../components/AssessmentCard";
import CreateAssessmentForm from "../components/CreateAssessmentForm";
import { CreateAssessmentRequest } from "../types/assessment.types";

export default function AssessmentsPage() {
  const { assessments, loading, error, refetch } = useAssessments();
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [filter, setFilter] = useState<"all" | "draft" | "completed">("all");

  const handleCreate = async (data: CreateAssessmentRequest) => {
    try {
      await assessmentApi.create(data);
      setShowCreateForm(false);
      refetch();
    } catch (err) {
      console.error("Failed to create assessment:", err);
      alert("Failed to create assessment");
    }
  };

  const handleComplete = async (id: number) => {
    if (confirm("Are you sure you want to complete this assessment?")) {
      try {
        await assessmentApi.complete(id);
        refetch();
      } catch (err) {
        console.error("Failed to complete assessment:", err);
        alert("Failed to complete assessment");
      }
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm("Are you sure you want to delete this assessment?")) {
      try {
        await assessmentApi.delete(id);
        refetch();
      } catch (err) {
        console.error("Failed to delete assessment:", err);
        alert("Failed to delete assessment");
      }
    }
  };

  const filteredAssessments = assessments.filter((assessment) => {
    if (filter === "draft") return assessment.status === "DRAFT";
    if (filter === "completed") return assessment.status === "COMPLETED";
    return true;
  });

  return (
    <div className="p-6 max-w-7xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold">Damage Assessments</h1>
          <p className="text-gray-600">Manage field assessment reports</p>
        </div>
        <button
          onClick={() => setShowCreateForm(!showCreateForm)}
          className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          {showCreateForm ? "Cancel" : "Create Assessment"}
        </button>
      </div>

      {showCreateForm && (
        <div className="mb-6">
          <CreateAssessmentForm
            onSubmit={handleCreate}
            onCancel={() => setShowCreateForm(false)}
          />
        </div>
      )}

      <div className="mb-4 flex gap-2">
        <button
          onClick={() => setFilter("all")}
          className={`px-4 py-2 rounded ${
            filter === "all" ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-700"
          }`}
        >
          All ({assessments.length})
        </button>
        <button
          onClick={() => setFilter("draft")}
          className={`px-4 py-2 rounded ${
            filter === "draft" ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-700"
          }`}
        >
          Draft ({assessments.filter((a) => a.status === "DRAFT").length})
        </button>
        <button
          onClick={() => setFilter("completed")}
          className={`px-4 py-2 rounded ${
            filter === "completed" ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-700"
          }`}
        >
          Completed ({assessments.filter((a) => a.status === "COMPLETED").length})
        </button>
      </div>

      {loading && (
        <div className="text-center py-8">
          <p className="text-gray-600">Loading assessments...</p>
        </div>
      )}

      {error && (
        <div className="bg-red-50 border border-red-200 rounded p-4 mb-4">
          <p className="text-red-600">{error}</p>
        </div>
      )}

      {!loading && !error && filteredAssessments.length === 0 && (
        <div className="text-center py-8 bg-gray-50 rounded">
          <p className="text-gray-600">No assessments found</p>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredAssessments.map((assessment) => (
          <AssessmentCard
            key={assessment.id}
            assessment={assessment}
            onComplete={handleComplete}
            onDelete={handleDelete}
          />
        ))}
      </div>
    </div>
  );
}
