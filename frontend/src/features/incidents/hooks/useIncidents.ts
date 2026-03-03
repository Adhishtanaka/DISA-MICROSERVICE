import { useEffect, useState } from "react";
import { incidentApi } from "../api/incidentApi";
import type { Incident, IncidentRequest, EscalateRequest, IncidentStatus } from "../types/incident.types";

export function useIncidents() {
  const [incidents, setIncidents] = useState<Incident[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchIncidents();
  }, []);

  async function fetchIncidents() {
    try {
      setLoading(true);
      const data = await incidentApi.getAll(0, 100);
      setIncidents(data.content);
    } catch {
      setError("Failed to load incidents");
    } finally {
      setLoading(false);
    }
  }

  async function createIncident(data: IncidentRequest) {
    const created = await incidentApi.create(data);
    setIncidents((prev) => [created, ...prev]);
    return created;
  }

  async function updateIncident(id: number, data: IncidentRequest) {
    const updated = await incidentApi.update(id, data);
    setIncidents((prev) => prev.map((i) => (i.id === id ? updated : i)));
    return updated;
  }

  async function escalateIncident(id: number, data: EscalateRequest) {
    const updated = await incidentApi.escalate(id, data);
    setIncidents((prev) => prev.map((i) => (i.id === id ? updated : i)));
    return updated;
  }

  async function changeStatus(id: number, status: IncidentStatus) {
    const updated = await incidentApi.updateStatus(id, status);
    setIncidents((prev) => prev.map((i) => (i.id === id ? updated : i)));
    return updated;
  }

  async function deleteIncident(id: number) {
    await incidentApi.remove(id);
    setIncidents((prev) => prev.filter((i) => i.id !== id));
  }

  return {
    incidents,
    loading,
    error,
    refetch: fetchIncidents,
    createIncident,
    updateIncident,
    escalateIncident,
    changeStatus,
    deleteIncident,
  };
}