import { AlertTriangle } from 'lucide-react';
import { Modal } from '../../../components/ui/Modal';
import { Button } from '../../../components/ui/Button';
import { useDeleteMission } from '../hooks/useDeleteMission';
import type { Mission } from '../types/mission.types';

interface DeleteConfirmModalProps {
  mission: Mission | null;
  onClose: () => void;
}

export function DeleteConfirmModal({ mission, onClose }: DeleteConfirmModalProps) {
  const { deleteMission, isDeleting, error } = useDeleteMission();

  if (!mission) return null;

  const handleConfirm = async () => {
    const ok = await deleteMission(mission.id);
    if (ok) onClose();
  };

  return (
    <Modal isOpen={!!mission} onClose={onClose} title="Delete Mission" size="sm">
      <div className="flex flex-col gap-4">
        <div className="flex items-start gap-3">
          <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-red-100">
            <AlertTriangle className="h-5 w-5 text-red-600" />
          </div>
          <div>
            <p className="text-sm text-gray-700">
              Are you sure you want to delete mission{' '}
              <span className="font-semibold">{mission.missionCode}</span>? This action cannot be
              undone.
            </p>
          </div>
        </div>

        {error && (
          <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700 border border-red-200">
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
