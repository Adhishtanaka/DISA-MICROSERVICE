import { type FormEvent, useState } from 'react';
import { Modal } from '../../../components/ui/Modal';
import { Select } from '../../../components/ui/Select';
import { Button } from '../../../components/ui/Button';
import { MissionStatusBadge } from './MissionBadges';
import { useUpdateMissionStatus } from '../hooks/useUpdateMissionStatus';
import type { Mission, MissionStatus } from '../types/mission.types';

const NEXT_STATUSES: Record<MissionStatus, { value: MissionStatus; label: string }[]> = {
  PENDING: [
    { value: 'IN_PROGRESS', label: 'In Progress' },
    { value: 'CANCELLED', label: 'Cancelled' },
  ],
  IN_PROGRESS: [
    { value: 'COMPLETED', label: 'Completed' },
    { value: 'CANCELLED', label: 'Cancelled' },
  ],
  COMPLETED: [],
  CANCELLED: [],
};

interface UpdateStatusModalProps {
  mission: Mission | null;
  onClose: () => void;
}

export function UpdateStatusModal({ mission, onClose }: UpdateStatusModalProps) {
  const { updateStatus, isSubmitting, error } = useUpdateMissionStatus();
  const [status, setStatus] = useState<MissionStatus>('IN_PROGRESS');
  const [remarks, setRemarks] = useState('');

  if (!mission) return null;

  const options = NEXT_STATUSES[mission.status] ?? [];

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const ok = await updateStatus(mission.id, status, remarks);
    if (ok) {
      setRemarks('');
      onClose();
    }
  };

  return (
    <Modal isOpen={!!mission} onClose={onClose} title="Update Mission Status" size="sm">
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        {/* Current status */}
        <div className="flex items-center gap-2 text-sm text-gray-600">
          <span>Current:</span>
          <MissionStatusBadge status={mission.status} />
        </div>

        {error && (
          <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700 border border-red-200">
            {error}
          </div>
        )}

        {options.length === 0 ? (
          <p className="text-sm text-gray-500">This mission cannot be transitioned further.</p>
        ) : (
          <>
            <Select
              label="New Status"
              options={options}
              value={status}
              onChange={(e) => setStatus(e.target.value as MissionStatus)}
            />

            <div className="flex flex-col gap-1.5">
              <label className="text-sm font-medium text-gray-700">Remarks (optional)</label>
              <textarea
                rows={2}
                placeholder="Add a note about this status change…"
                value={remarks}
                onChange={(e) => setRemarks(e.target.value)}
                className="w-full rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm text-gray-900 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/30 resize-none"
              />
            </div>

            <div className="flex justify-end gap-3">
              <Button variant="outline" type="button" onClick={onClose} disabled={isSubmitting}>
                Cancel
              </Button>
              <Button type="submit" isLoading={isSubmitting}>
                Update
              </Button>
            </div>
          </>
        )}
      </form>
    </Modal>
  );
}
