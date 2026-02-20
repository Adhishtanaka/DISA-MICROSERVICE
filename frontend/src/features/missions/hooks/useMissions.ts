import { useCallback, useEffect } from 'react';
import { missionApi } from '../api/missionApi';
import { useMissionStore } from '../store/missionStore';

/**
 * Fetches all missions on mount and keeps the store in sync.
 */
export function useMissions() {
  const { missions, filters, isLoading, error, setMissions, setLoading, setError } =
    useMissionStore();

  const fetchMissions = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await missionApi.getAll();
      setMissions(data);
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : 'Failed to load missions';
      setError(message);
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError, setMissions]);

  useEffect(() => {
    fetchMissions();
  }, [fetchMissions]);

  /** Client-side filtering */
  const filtered = missions.filter((m) => {
    const matchStatus = filters.status === 'ALL' || m.status === filters.status;
    const matchType = filters.type === 'ALL' || m.type === filters.type;
    const q = filters.search.toLowerCase();
    const matchSearch =
      !q ||
      m.missionCode.toLowerCase().includes(q) ||
      m.origin.toLowerCase().includes(q) ||
      m.destination.toLowerCase().includes(q);
    return matchStatus && matchType && matchSearch;
  });

  return { missions: filtered, isLoading, error, refetch: fetchMissions };
}
