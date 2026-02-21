import { useState } from 'react';
import { resourceApi } from '../api/resourceApi';
import { useResourceStore } from '../store/resourceStore';
import type { StockOperation } from '../types/resource.types';

export function useStockOperation() {
  const { updateResource } = useResourceStore();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const applyStock = async (
    id: number,
    operation: StockOperation,
    quantity: number,
  ): Promise<boolean> => {
    setIsSubmitting(true);
    setError(null);
    try {
      let updated;
      if (operation === 'SET')       updated = await resourceApi.setStock(id, { quantity });
      else if (operation === 'INCREMENT') updated = await resourceApi.incrementStock(id, { quantity });
      else                           updated = await resourceApi.decrementStock(id, { quantity });
      updateResource(updated);
      return true;
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to update stock');
      return false;
    } finally {
      setIsSubmitting(false);
    }
  };

  return { applyStock, isSubmitting, error };
}
