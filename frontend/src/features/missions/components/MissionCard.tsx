import { MapPin, Truck, ChevronRight, Calendar } from 'lucide-react';
import { Card, CardBody } from '../../../components/ui/Card';
import { Button } from '../../../components/ui/Button';
import { MissionStatusBadge, MissionTypeBadge } from './MissionBadges';
import type { Mission } from '../types/mission.types';

interface MissionCardProps {
  mission: Mission;
  onViewDetails: (mission: Mission) => void;
  onUpdateStatus: (mission: Mission) => void;
  onDelete: (mission: Mission) => void;
}

function formatDate(dateStr: string | null): string {
  if (!dateStr) return '—';
  return new Date(dateStr).toLocaleDateString('en-US', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  });
}

export function MissionCard({
  mission,
  onViewDetails,
  onUpdateStatus,
  onDelete,
}: MissionCardProps) {
  return (
    <Card className="transition-shadow hover:shadow-md">
      <CardBody>
        {/* Header row */}
        <div className="flex items-start justify-between gap-3">
          <div>
            <p className="text-xs font-mono text-gray-400">{mission.missionCode}</p>
            <h3 className="mt-0.5 text-sm font-semibold text-gray-900">
              {mission.origin} → {mission.destination}
            </h3>
          </div>
          <MissionStatusBadge status={mission.status} />
        </div>

        {/* Type + vehicle */}
        <div className="mt-3 flex flex-wrap items-center gap-2">
          <MissionTypeBadge type={mission.type} />
          {mission.vehicleType && (
            <span className="inline-flex items-center gap-1 text-xs text-gray-500">
              <Truck className="h-3 w-3" />
              {mission.vehicleType}
            </span>
          )}
        </div>

        {/* Route */}
        <div className="mt-3 flex items-center gap-1 text-xs text-gray-500">
          <MapPin className="h-3 w-3 shrink-0 text-blue-400" />
          <span>{mission.origin}</span>
          <ChevronRight className="h-3 w-3" />
          <span>{mission.destination}</span>
        </div>

        {/* Description */}
        {mission.description && (
          <p className="mt-2 line-clamp-2 text-xs text-gray-500">{mission.description}</p>
        )}

        {/* Date */}
        <div className="mt-3 flex items-center gap-1 text-xs text-gray-400">
          <Calendar className="h-3 w-3" />
          Created {formatDate(mission.createdAt)}
        </div>

        {/* Actions */}
        <div className="mt-4 flex gap-2 border-t border-gray-100 pt-3">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => onViewDetails(mission)}
            className="flex-1"
          >
            Details
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => onUpdateStatus(mission)}
            className="flex-1"
            disabled={mission.status === 'COMPLETED' || mission.status === 'CANCELLED'}
          >
            Update Status
          </Button>
          <Button
            variant="danger"
            size="sm"
            onClick={() => onDelete(mission)}
          >
            Delete
          </Button>
        </div>
      </CardBody>
    </Card>
  );
}
