import { useEffect, useState } from "react";
import { incidentApi } from "../api/incidentApi";
import { Incident } from "../types/incident.types";

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
      const data = await incidentApi.getAll();
      setIncidents(data.content);
    } catch (err) {
      setError("Failed to load incidents");
    } finally {
      setLoading(false);
    }
  }

  return { incidents, loading, error, refetch: fetchIncidents };
}