import { AlertTriangle } from 'lucide-react';
import { Modal } from '../../../components/ui/Modal';
import { Button } from '../../../components/ui/Button';
import { useDeleteResource } from '../hooks/useDeleteResource';
import type { Resource } from '../types/resource.types';

interface DeleteConfirmModalProps {
  resource: Resource | null;
  onClose: () => void;
}

export function DeleteConfirmModal({ resource, onClose }: DeleteConfirmModalProps) {
  const { deleteResource, isDeleting, error } = useDeleteResource();

  if (!resource) return null;

  const handleConfirm = async () => {
    const ok = await deleteResource(resource.id);
    if (ok) onClose();
  };

  return (
    <Modal isOpen={!!resource} onClose={onClose} title="Delete Resource" size="sm">
      <div className="flex flex-col gap-4">
        <div className="flex items-start gap-3">
          <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-red-100">
            <AlertTriangle className="h-5 w-5 text-red-600" />
          </div>
          <p className="text-sm text-gray-700">
            Are you sure you want to delete{' '}
            <span className="font-semibold">{resource.name}</span>{' '}
            <span className="font-mono text-gray-400">({resource.resourceCode})</span>? This action
            cannot be undone.
          </p>
        </div>

        {error && (
          <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <div className="flex justify-end gap-3">
          <Button variant="outline" onClick={onClose} disabled={isDeleting}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleConfirm} isLoading={isDeleting}>
            Delete
          </Button>
        </div>
      </div>
    </Modal>
  );
}
