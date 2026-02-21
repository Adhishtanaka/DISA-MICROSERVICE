import { useState } from 'react';
import { Plus, RefreshCw, Database, AlertTriangle, Package, CheckCircle } from 'lucide-react';
import { ResourceFilters } from '../components/ResourceFilters';
import { ResourceList } from '../components/ResourceList';
import { ResourceForm } from '../components/ResourceForm';
import { Modal } from '../../../components/ui/Modal';
import { Button } from '../../../components/ui/Button';
import { useResources } from '../hooks/useResources';
import { useResourceStore } from '../store/resourceStore';

export function ResourcesPage() {
  const { resources: filtered, isLoading, error, refetch } = useResources();
  const { resources: all } = useResourceStore();
  const [isCreateOpen, setIsCreateOpen] = useState(false);

  const stats = {
    total:    all.length,
    lowStock: all.filter((r) => r.currentStock <= r.threshold).length,
    healthy:  all.filter((r) => r.currentStock > r.threshold).length,
    types:    new Set(all.map((r) => r.type)).size,
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="sticky top-0 z-20 border-b border-gray-200 bg-white px-6 py-4">
        <div className="mx-auto flex max-w-7xl items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-emerald-600">
              <Database className="h-5 w-5 text-white" />
            </div>
            <div>
              <h1 className="text-base font-semibold text-gray-900">Resource Inventory</h1>
              <p className="text-xs text-gray-500">Disaster Management — Supply Chain</p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Button variant="ghost" size="sm" onClick={refetch} disabled={isLoading}>
              <RefreshCw className={`h-4 w-4 ${isLoading ? 'animate-spin' : ''}`} />
              Refresh
            </Button>
            <Button size="sm" onClick={() => setIsCreateOpen(true)}>
              <Plus className="h-4 w-4" />
              New Resource
            </Button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-7xl space-y-6 px-6 py-6">
        {/* Stats */}
        <div className="grid grid-cols-2 gap-3 sm:grid-cols-4">
          <StatCard
            label="Total Resources"
            value={stats.total}
            icon={<Package className="h-4 w-4 text-gray-500" />}
            color="bg-white"
            textColor="text-gray-900"
          />
          <StatCard
            label="Low Stock"
            value={stats.lowStock}
            icon={<AlertTriangle className="h-4 w-4 text-red-500" />}
            color={stats.lowStock > 0 ? 'bg-red-50' : 'bg-white'}
            textColor={stats.lowStock > 0 ? 'text-red-700' : 'text-gray-900'}
          />
          <StatCard
            label="Healthy Stock"
            value={stats.healthy}
            icon={<CheckCircle className="h-4 w-4 text-green-500" />}
            color="bg-green-50"
            textColor="text-green-700"
          />
          <StatCard
            label="Categories"
            value={stats.types}
            icon={<Database className="h-4 w-4 text-blue-500" />}
            color="bg-blue-50"
            textColor="text-blue-700"
          />
        </div>

        {/* Low stock alert banner */}
        {stats.lowStock > 0 && (
          <div className="flex items-center gap-3 rounded-xl border border-red-200 bg-red-50 px-4 py-3">
            <AlertTriangle className="h-5 w-5 shrink-0 text-red-600" />
            <p className="text-sm text-red-700">
              <span className="font-semibold">{stats.lowStock} resource{stats.lowStock > 1 ? 's' : ''}</span>{' '}
              {stats.lowStock > 1 ? 'are' : 'is'} at or below the low-stock threshold. Restock soon.
            </p>
          </div>
        )}

        {/* Filters */}
        <ResourceFilters />

        {/* Count */}
        {!isLoading && !error && (
          <p className="text-sm text-gray-500">
            Showing{' '}
            <span className="font-medium text-gray-700">{filtered.length}</span> resource
            {filtered.length !== 1 ? 's' : ''}
          </p>
        )}

        {/* List */}
        <ResourceList resources={filtered} isLoading={isLoading} error={error} />
      </main>

      {/* Create modal */}
      <Modal
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        title="Add New Resource"
        size="lg"
      >
        <ResourceForm
          onSuccess={() => setIsCreateOpen(false)}
          onCancel={() => setIsCreateOpen(false)}
        />
      </Modal>
    </div>
  );
}

function StatCard({
  label,
  value,
  icon,
  color,
  textColor,
}: {
  label: string;
  value: number;
  icon: React.ReactNode;
  color: string;
  textColor: string;
}) {
  return (
    <div className={`${color} rounded-xl border border-gray-200 px-4 py-3`}>
      <div className="flex items-center gap-1.5">
        {icon}
        <p className="text-xs text-gray-500">{label}</p>
      </div>
      <p className={`mt-1 text-2xl font-bold ${textColor}`}>{value}</p>
    </div>
  );
}
