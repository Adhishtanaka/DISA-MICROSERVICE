import { type FormEvent, useState } from 'react';
import { Modal } from '../../../components/ui/Modal';
import { Select } from '../../../components/ui/Select';
import { Input } from '../../../components/ui/Input';
import { Button } from '../../../components/ui/Button';
import { StockStatusBadge } from './ResourceBadges';
import { useStockOperation } from '../hooks/useStockOperation';
import type { Resource, StockOperation } from '../types/resource.types';

const OPERATION_OPTIONS = [
  { value: 'INCREMENT', label: '+ Increment (add to stock)' },
  { value: 'DECREMENT', label: '− Decrement (remove from stock)' },
  { value: 'SET',       label: '= Set (absolute value)' },
];

interface StockUpdateModalProps {
  resource: Resource | null;
  onClose: () => void;
}

export function StockUpdateModal({ resource, onClose }: StockUpdateModalProps) {
  const { applyStock, isSubmitting, error } = useStockOperation();
  const [operation, setOperation] = useState<StockOperation>('INCREMENT');
  const [quantity, setQuantity] = useState<number>(1);

  if (!resource) return null;

  const previewStock = (): number => {
    if (operation === 'SET')       return quantity;
    if (operation === 'INCREMENT') return resource.currentStock + quantity;
    return Math.max(0, resource.currentStock - quantity);
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const ok = await applyStock(resource.id, operation, quantity);
    if (ok) {
      setQuantity(1);
      onClose();
    }
  };

  return (
    <Modal isOpen={!!resource} onClose={onClose} title="Update Stock" size="sm">
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        {/* Current state */}
        <div className="flex items-center justify-between rounded-xl border border-gray-100 bg-gray-50 px-4 py-3">
          <div>
            <p className="text-xs text-gray-500">Current Stock</p>
            <p className="text-lg font-bold text-gray-900">
              {resource.currentStock}{' '}
              <span className="text-sm font-normal text-gray-500">{resource.unit}</span>
            </p>
          </div>
          <StockStatusBadge current={resource.currentStock} threshold={resource.threshold} />
        </div>

        {error && (
          <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        {/* Operation */}
        <Select
          label="Operation"
          options={OPERATION_OPTIONS}
          value={operation}
          onChange={(e) => setOperation(e.target.value as StockOperation)}
        />

        {/* Quantity */}
        <Input
          label="Quantity"
          type="number"
          min={operation === 'SET' ? 0 : 1}
          value={quantity}
          onChange={(e) => setQuantity(Number(e.target.value))}
          required
        />

        {/* Preview */}
        <div className="rounded-lg border border-blue-100 bg-blue-50 px-4 py-3 text-sm">
          <span className="text-blue-600 font-medium">Preview: </span>
          <span className="text-blue-900 font-bold">{previewStock()} {resource.unit}</span>
          {previewStock() <= resource.threshold && (
            <span className="ml-2 text-xs text-red-600 font-medium">⚠ Below threshold ({resource.threshold})</span>
          )}
        </div>

        <div className="flex justify-end gap-3">
          <Button variant="outline" type="button" onClick={onClose} disabled={isSubmitting}>
            Cancel
          </Button>
          <Button type="submit" isLoading={isSubmitting}>
            Apply
          </Button>
        </div>
      </form>
    </Modal>
  );
}
