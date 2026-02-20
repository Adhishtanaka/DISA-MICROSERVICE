import { Search, Filter } from 'lucide-react';
import { Select } from '../../../components/ui/Select';
import { useMissionStore } from '../store/missionStore';
import type { MissionStatus, MissionType } from '../types/mission.types';

const STATUS_OPTIONS = [
  { value: 'ALL', label: 'All Statuses' },
  { value: 'PENDING', label: 'Pending' },
  { value: 'IN_PROGRESS', label: 'In Progress' },
  { value: 'COMPLETED', label: 'Completed' },
  { value: 'CANCELLED', label: 'Cancelled' },
];

const TYPE_OPTIONS = [
  { value: 'ALL', label: 'All Types' },
  { value: 'DELIVERY', label: 'Delivery' },
  { value: 'RESCUE', label: 'Rescue' },
  { value: 'EVACUATION', label: 'Evacuation' },
  { value: 'MEDICAL_TRANSPORT', label: 'Medical Transport' },
  { value: 'ASSESMENT', label: 'Assessment' },
];

export function MissionFilters() {
  const { filters, setFilters } = useMissionStore();

  return (
    <div className="flex flex-col gap-3 sm:flex-row sm:items-end">
      {/* Search */}
      <div className="relative flex-1">
        <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
        <input
          type="text"
          placeholder="Search by code, origin or destination…"
          value={filters.search}
          onChange={(e) => setFilters({ search: e.target.value })}
          className="w-full rounded-lg border border-gray-300 bg-white py-2 pl-9 pr-3 text-sm text-gray-900 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/30"
        />
      </div>

      {/* Status filter */}
      <div className="flex items-center gap-2">
        <Filter className="h-4 w-4 text-gray-400 shrink-0" />
        <Select
          options={STATUS_OPTIONS}
          value={filters.status}
          onChange={(e) => setFilters({ status: e.target.value as MissionStatus | 'ALL' })}
          className="min-w-[140px]"
        />
      </div>

      {/* Type filter */}
      <Select
        options={TYPE_OPTIONS}
        value={filters.type}
        onChange={(e) => setFilters({ type: e.target.value as MissionType | 'ALL' })}
        className="min-w-[160px]"
      />
    </div>
  );
}
