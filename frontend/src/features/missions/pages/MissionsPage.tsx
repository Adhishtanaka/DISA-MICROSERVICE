import { useState } from 'react';
import { Plus, RefreshCw, Activity } from 'lucide-react';
import { MissionFilters } from '../components/MissionFilters';
import { MissionList } from '../components/MissionList';
import { MissionForm } from '../components/MissionForm';
import { Modal } from '../../../components/ui/Modal';
import { Button } from '../../../components/ui/Button';
import { useMissions } from '../hooks/useMissions';
import { useMissionStore } from '../store/missionStore';

export function MissionsPage() {
  const { missions, isLoading, error, refetch } = useMissions();
  const { missions: all } = useMissionStore();
  const [isCreateOpen, setIsCreateOpen] = useState(false);

  // Summary stats
  const stats = {
    total: all.length,
    pending: all.filter((m) => m.status === 'PENDING').length,
    inProgress: all.filter((m) => m.status === 'IN_PROGRESS').length,
    completed: all.filter((m) => m.status === 'COMPLETED').length,
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Top bar */}
      <header className="sticky top-0 z-20 border-b border-gray-200 bg-white px-6 py-4">
        <div className="mx-auto flex max-w-7xl items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-blue-600">
              <Activity className="h-5 w-5 text-white" />
            </div>
            <div>
              <h1 className="text-base font-semibold text-gray-900">Mission Control</h1>
              <p className="text-xs text-gray-500">Disaster Management — Logistics</p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={refetch}
              disabled={isLoading}
            >
              <RefreshCw className={`h-4 w-4 ${isLoading ? 'animate-spin' : ''}`} />
              Refresh
            </Button>
            <Button size="sm" onClick={() => setIsCreateOpen(true)}>
              <Plus className="h-4 w-4" />
              New Mission
            </Button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-7xl px-6 py-6 space-y-6">
        {/* Stats */}
        <div className="grid grid-cols-2 gap-3 sm:grid-cols-4">
          <StatCard label="Total" value={stats.total} color="bg-white" textColor="text-gray-900" />
          <StatCard label="Pending" value={stats.pending} color="bg-yellow-50" textColor="text-yellow-700" />
          <StatCard label="In Progress" value={stats.inProgress} color="bg-blue-50" textColor="text-blue-700" />
          <StatCard label="Completed" value={stats.completed} color="bg-green-50" textColor="text-green-700" />
        </div>

        {/* Filters */}
        <MissionFilters />

        {/* Results count */}
        {!isLoading && !error && (
          <p className="text-sm text-gray-500">
            Showing <span className="font-medium text-gray-700">{missions.length}</span> mission
            {missions.length !== 1 ? 's' : ''}
          </p>
        )}

        {/* List */}
        <MissionList missions={missions} isLoading={isLoading} error={error} />
      </main>

      {/* Create modal */}
      <Modal
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        title="Create New Mission"
        size="lg"
      >
        <MissionForm
          onSuccess={() => setIsCreateOpen(false)}
          onCancel={() => setIsCreateOpen(false)}
        />
      </Modal>
    </div>
  );
}

// ─── Local helper ─────────────────────────────────────────────────────────────

function StatCard({
  label,
  value,
  color,
  textColor,
}: {
  label: string;
  value: number;
  color: string;
  textColor: string;
}) {
  return (
    <div className={`${color} rounded-xl border border-gray-200 px-4 py-3`}>
      <p className="text-xs text-gray-500">{label}</p>
      <p className={`mt-1 text-2xl font-bold ${textColor}`}>{value}</p>
    </div>
  );
}
