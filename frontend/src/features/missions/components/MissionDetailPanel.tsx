import type { ReactNode } from 'react';
import { X, Truck, MapPin, Calendar, Tag, ClipboardList } from 'lucide-react';
import { MissionStatusBadge, MissionTypeBadge } from './MissionBadges';
import { Button } from '../../../components/ui/Button';
import type { Mission } from '../types/mission.types';

interface MissionDetailPanelProps {
  mission: Mission;
  onClose: () => void;
  onUpdateStatus: (mission: Mission) => void;
  onDelete: (mission: Mission) => void;
}

function DetailRow({ label, value }: { label: string; value: ReactNode }) {
  return (
    <div>
      <p className="text-xs font-medium uppercase tracking-wide text-gray-400">{label}</p>
      <p className="mt-0.5 text-sm text-gray-700">{value ?? '—'}</p>
    </div>
  );
}

function formatDateTime(dateStr: string | null): string {
  if (!dateStr) return '—';
  return new Date(dateStr).toLocaleString('en-US', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

export function MissionDetailPanel({
  mission,
  onClose,
  onUpdateStatus,
  onDelete,
}: MissionDetailPanelProps) {
  const canUpdate = mission.status !== 'COMPLETED' && mission.status !== 'CANCELLED';

  return (
    <div className="fixed inset-y-0 right-0 z-40 flex w-full max-w-md flex-col bg-white shadow-2xl ring-1 ring-black/5">
      {/* Header */}
      <div className="flex items-center justify-between border-b border-gray-100 px-6 py-4">
        <div>
          <p className="text-xs font-mono text-gray-400">{mission.missionCode}</p>
          <h2 className="text-base font-semibold text-gray-900">Mission Details</h2>
        </div>
        <button
          onClick={onClose}
          className="rounded-lg p-1.5 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors"
        >
          <X className="h-4 w-4" />
        </button>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto px-6 py-5 space-y-6">
        {/* Status & Type */}
        <div className="flex flex-wrap items-center gap-2">
          <MissionStatusBadge status={mission.status} />
          <MissionTypeBadge type={mission.type} />
        </div>

        {/* Route */}
        <section>
          <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
            <MapPin className="h-3.5 w-3.5" /> Route
          </h3>
          <div className="grid grid-cols-2 gap-4">
            <DetailRow label="Origin" value={mission.origin} />
            <DetailRow label="Destination" value={mission.destination} />
          </div>
        </section>

        {/* Vehicle */}
        <section>
          <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
            <Truck className="h-3.5 w-3.5" /> Vehicle & Driver
          </h3>
          <div className="grid grid-cols-2 gap-4">
            <DetailRow label="Vehicle ID" value={mission.vehicleId} />
            <DetailRow label="Vehicle Type" value={mission.vehicleType} />
            <DetailRow label="Driver ID" value={mission.driverId} />
            <DetailRow label="Driver Name" value={mission.driverName} />
          </div>
        </section>

        {/* Details */}
        <section>
          <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
            <ClipboardList className="h-3.5 w-3.5" /> Details
          </h3>
          <div className="space-y-3">
            <DetailRow label="Description" value={mission.description} />
            <DetailRow label="Cargo Details" value={mission.cargoDetails} />
            <DetailRow label="Notes" value={mission.notes} />
          </div>
        </section>

        {/* References */}
        {(mission.incidentId || mission.resourceId) && (
          <section>
            <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
              <Tag className="h-3.5 w-3.5" /> References
            </h3>
            <div className="grid grid-cols-2 gap-4">
              <DetailRow label="Incident ID" value={mission.incidentId} />
              <DetailRow label="Resource ID" value={mission.resourceId} />
            </div>
          </section>
        )}

        {/* Timestamps */}
        <section>
          <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
            <Calendar className="h-3.5 w-3.5" /> Timeline
          </h3>
          <div className="space-y-3">
            <DetailRow label="Created At" value={formatDateTime(mission.createdAt)} />
            <DetailRow label="Started At" value={formatDateTime(mission.startedAt)} />
            <DetailRow label="Completed At" value={formatDateTime(mission.completedAt)} />
          </div>
        </section>
      </div>

      {/* Footer actions */}
      <div className="flex gap-3 border-t border-gray-100 px-6 py-4">
        {canUpdate && (
          <Button
            variant="outline"
            size="sm"
            onClick={() => onUpdateStatus(mission)}
            className="flex-1"
          >
            Update Status
          </Button>
        )}
        <Button
          variant="danger"
          size="sm"
          onClick={() => onDelete(mission)}
          className={canUpdate ? '' : 'flex-1'}
        >
          Delete
        </Button>
      </div>
    </div>
  );
}
