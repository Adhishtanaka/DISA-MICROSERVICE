import { useEffect, useState } from "react";
import { assessmentApi } from "../api/assessmentApi";
import { Assessment } from "../types/assessment.types";

export function useAssessments() {
  const [assessments, setAssessments] = useState<Assessment[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchAssessments();
  }, []);

  async function fetchAssessments() {
    try {
      setLoading(true);
      setError(null);
      const data = await assessmentApi.getAll();
      setAssessments(data);
    } catch (err) {
      setError("Failed to load assessments");
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  return { assessments, loading, error, refetch: fetchAssessments };
}

export function useAssessmentsByIncident(incidentId: number) {
  const [assessments, setAssessments] = useState<Assessment[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (incidentId) {
      fetchAssessments();
    }
  }, [incidentId]);

  async function fetchAssessments() {
    try {
      setLoading(true);
      setError(null);
      const data = await assessmentApi.getByIncident(incidentId);
      setAssessments(data);
    } catch (err) {
      setError("Failed to load assessments");
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  return { assessments, loading, error, refetch: fetchAssessments };
}
