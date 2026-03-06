import { useEffect, useState } from "react";
import { shelterApi } from "../api/shelterApi";
import type { Shelter } from "../types/shelter.types";

export function useShelters(availableOnly = false) {
  const [shelters, setShelters] = useState<Shelter[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchShelters();
  }, [availableOnly]);

  async function fetchShelters() {
    try {
      setLoading(true);
      setError(null);
      const data = availableOnly 
        ? await shelterApi.getAvailable() 
        : await shelterApi.getAll();
      setShelters(Array.isArray(data) ? data : []);
    } catch (err) {
      setError("Failed to load shelters");
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  return { shelters, loading, error, refetch: fetchShelters };
}
