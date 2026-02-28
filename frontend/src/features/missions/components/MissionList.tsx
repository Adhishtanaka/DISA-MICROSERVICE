import { useState } from 'react';
import { Package } from 'lucide-react';
import { MissionCard } from './MissionCard';
import { MissionDetailPanel } from './MissionDetailPanel';
import { UpdateStatusModal } from './UpdateStatusModal';
import { DeleteConfirmModal } from './DeleteConfirmModal';
import { Spinner } from '../../../components/ui/Spinner';
import type { Mission } from '../types/mission.types';

interface MissionListProps {
  missions: Mission[];
  isLoading: boolean;
  error: string | null;
}

export function MissionList({ missions, isLoading, error }: MissionListProps) {
  const [detailMission, setDetailMission] = useState<Mission | null>(null);
  const [statusMission, setStatusMission] = useState<Mission | null>(null);
  const [deleteMission, setDeleteMission] = useState<Mission | null>(null);

  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center gap-3 py-20 text-gray-500">
        <Spinner size="lg" />
        <p className="text-sm">Loading missions…</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center gap-2 py-20 text-center">
        <p className="font-medium text-red-600">Failed to load missions</p>
        <p className="text-sm text-gray-500">{error}</p>
      </div>
    );
  }

  if (missions.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center gap-3 py-20 text-gray-400">
        <Package className="h-12 w-12 opacity-30" />
        <p className="text-sm">No missions found. Try adjusting your filters.</p>
      </div>
    );
  }

  return (
    <>
      {/* Grid */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {missions.map((mission) => (
          <MissionCard
            key={mission.id}
            mission={mission}
            onViewDetails={setDetailMission}
            onUpdateStatus={setStatusMission}
            onDelete={setDeleteMission}
          />
        ))}
      </div>

      {/* Side panel */}
      {detailMission && (
        <>
          <div
            className="fixed inset-0 z-30 bg-black/20"
            onClick={() => setDetailMission(null)}
          />
          <MissionDetailPanel
            mission={detailMission}
            onClose={() => setDetailMission(null)}
            onUpdateStatus={(m) => {
              setDetailMission(null);
              setStatusMission(m);
            }}
            onDelete={(m) => {
              setDetailMission(null);
              setDeleteMission(m);
            }}
          />
        </>
      )}

      {/* Status modal */}
      <UpdateStatusModal
        mission={statusMission}
        onClose={() => setStatusMission(null)}
      />

      {/* Delete confirmation */}
      <DeleteConfirmModal
        mission={deleteMission}
        onClose={() => setDeleteMission(null)}
      />
    </>
  );
}
