import { useState } from 'react';
import { resourceApi } from '../api/resourceApi';
import { useResourceStore } from '../store/resourceStore';
import type { CreateResourceRequest } from '../types/resource.types';

export function useCreateResource() {
  const { addResource } = useResourceStore();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const createResource = async (payload: CreateResourceRequest): Promise<boolean> => {
    setIsSubmitting(true);
    setError(null);
    try {
      const resource = await resourceApi.create(payload);
      addResource(resource);
      return true;
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to create resource');
      return false;
    } finally {
      setIsSubmitting(false);
    }
  };

  return { createResource, isSubmitting, error };
}
