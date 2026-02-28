import { Search, Filter, AlertTriangle } from 'lucide-react';
import { Select } from '../../../components/ui/Select';
import { useResourceStore } from '../store/resourceStore';
import type { ResourceType } from '../types/resource.types';

const TYPE_OPTIONS = [
  { value: 'ALL',       label: 'All Types' },
  { value: 'FOOD',      label: 'Food' },
  { value: 'WATER',     label: 'Water' },
  { value: 'MEDICINE',  label: 'Medicine' },
  { value: 'EQUIPMENT', label: 'Equipment' },
  { value: 'CLOTHING',  label: 'Clothing' },
  { value: 'HYGIENE',   label: 'Hygiene' },
];

export function ResourceFilters() {
  const { filters, setFilters } = useResourceStore();

  return (
    <div className="flex flex-col gap-3 sm:flex-row sm:items-end">
      {/* Search */}
      <div className="relative flex-1">
        <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
        <input
          type="text"
          placeholder="Search by name, code or location…"
          value={filters.search}
          onChange={(e) => setFilters({ search: e.target.value })}
          className="w-full rounded-lg border border-gray-300 bg-white py-2 pl-9 pr-3 text-sm text-gray-900 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/30"
        />
      </div>

      {/* Type filter */}
      <div className="flex items-center gap-2">
        <Filter className="h-4 w-4 text-gray-400 shrink-0" />
        <Select
          options={TYPE_OPTIONS}
          value={filters.type}
          onChange={(e) => setFilters({ type: e.target.value as ResourceType | 'ALL' })}
          className="min-w-[150px]"
        />
      </div>

      {/* Low stock toggle */}
      <label className="flex cursor-pointer items-center gap-2 rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm text-gray-700 hover:bg-gray-50 transition-colors">
        <input
          type="checkbox"
          checked={filters.lowStockOnly}
          onChange={(e) => setFilters({ lowStockOnly: e.target.checked })}
          className="h-4 w-4 rounded border-gray-300 text-red-600 focus:ring-red-500"
        />
        <AlertTriangle className="h-3.5 w-3.5 text-red-500" />
        Low Stock Only
      </label>
    </div>
  );
}
