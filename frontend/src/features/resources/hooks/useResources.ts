import { useCallback, useEffect } from 'react';
import { resourceApi } from '../api/resourceApi';
import { useResourceStore } from '../store/resourceStore';

export function useResources() {
  const { resources, filters, isLoading, error, setResources, setLoading, setError } =
    useResourceStore();

  const fetchResources = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await resourceApi.getAll();
      setResources(data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to load resources');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError, setResources]);

  useEffect(() => {
    fetchResources();
  }, [fetchResources]);

  const filtered = resources.filter((r) => {
    const matchType = filters.type === 'ALL' || r.type === filters.type;
    const matchLow = !filters.lowStockOnly || r.currentStock <= r.threshold;
    const q = filters.search.toLowerCase();
    const matchSearch =
      !q ||
      r.name.toLowerCase().includes(q) ||
      r.resourceCode.toLowerCase().includes(q) ||
      r.location.toLowerCase().includes(q);
    return matchType && matchLow && matchSearch;
  });

  return { resources: filtered, isLoading, error, refetch: fetchResources };
}
