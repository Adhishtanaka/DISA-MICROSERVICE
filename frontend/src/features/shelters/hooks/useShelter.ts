import { useEffect, useState } from "react";
import { shelterApi } from "../api/shelterApi";
import { Shelter } from "../types/shelter.types";

export function useShelter(id: number | null) {
  const [shelter, setShelter] = useState<Shelter | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      fetchShelter(id);
    }
  }, [id]);

  async function fetchShelter(shelterId: number) {
    try {
      setLoading(true);
      setError(null);
      const data = await shelterApi.getById(shelterId);
      setShelter(data);
    } catch (err) {
      setError("Failed to load shelter");
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  return { shelter, loading, error, refetch: () => id && fetchShelter(id) };
}
