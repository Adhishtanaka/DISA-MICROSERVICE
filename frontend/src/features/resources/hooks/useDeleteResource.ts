import { useState } from 'react';
import { resourceApi } from '../api/resourceApi';
import { useResourceStore } from '../store/resourceStore';

export function useDeleteResource() {
  const { removeResource } = useResourceStore();
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const deleteResource = async (id: number): Promise<boolean> => {
    setIsDeleting(true);
    setError(null);
    try {
      await resourceApi.remove(id);
      removeResource(id);
      return true;
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to delete resource');
      return false;
    } finally {
      setIsDeleting(false);
    }
  };

  return { deleteResource, isDeleting, error };
}
