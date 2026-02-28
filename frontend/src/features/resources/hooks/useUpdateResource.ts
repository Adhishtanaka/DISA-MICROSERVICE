import { useState } from 'react';
import { resourceApi } from '../api/resourceApi';
import { useResourceStore } from '../store/resourceStore';
import type { UpdateResourceRequest } from '../types/resource.types';

export function useUpdateResource() {
  const { updateResource } = useResourceStore();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const updateRes = async (id: number, payload: UpdateResourceRequest): Promise<boolean> => {
    setIsSubmitting(true);
    setError(null);
    try {
      const updated = await resourceApi.update(id, payload);
      updateResource(updated);
      return true;
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to update resource');
      return false;
    } finally {
      setIsSubmitting(false);
    }
  };

  return { updateRes, isSubmitting, error };
}
