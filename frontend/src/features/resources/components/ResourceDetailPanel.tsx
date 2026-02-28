import type { ReactNode } from 'react';
import { X, MapPin, Package, Calendar, Tag, BarChart3 } from 'lucide-react';
import { ResourceTypeBadge, StockStatusBadge } from './ResourceBadges';
import { Button } from '../../../components/ui/Button';
import type { Resource } from '../types/resource.types';

interface ResourceDetailPanelProps {
  resource: Resource;
  onClose: () => void;
  onEditStock: (resource: Resource) => void;
  onEdit: (resource: Resource) => void;
  onDelete: (resource: Resource) => void;
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

function StockProgress({ current, threshold }: { current: number; threshold: number }) {
  const max = Math.max(current, threshold) * 1.5 || 1;
  const pct = Math.min((current / max) * 100, 100);
  const isLow = current <= threshold;
  return (
    <div className="rounded-xl border border-gray-100 bg-gray-50 p-4">
      <div className="flex items-center justify-between text-sm">
        <span className="font-medium text-gray-700">Current Stock</span>
        <span className={`text-lg font-bold ${isLow ? 'text-red-600' : 'text-green-600'}`}>
          {current}
        </span>
      </div>
      <div className="mt-2 h-2 w-full overflow-hidden rounded-full bg-gray-200">
        <div
          className={`h-full rounded-full ${isLow ? 'bg-red-500' : 'bg-green-500'}`}
          style={{ width: `${pct}%` }}
        />
      </div>
      <p className="mt-1.5 text-xs text-gray-500">
        Threshold: <span className="font-medium text-gray-700">{threshold}</span>
      </p>
    </div>
  );
}

export function ResourceDetailPanel({
  resource,
  onClose,
  onEditStock,
  onEdit,
  onDelete,
}: ResourceDetailPanelProps) {
  return (
    <div className="fixed inset-y-0 right-0 z-40 flex w-full max-w-md flex-col bg-white shadow-2xl ring-1 ring-black/5">
      {/* Header */}
      <div className="flex items-center justify-between border-b border-gray-100 px-6 py-4">
        <div>
          <p className="text-xs font-mono text-gray-400">{resource.resourceCode}</p>
          <h2 className="text-base font-semibold text-gray-900">Resource Details</h2>
        </div>
        <button
          onClick={onClose}
          className="rounded-lg p-1.5 text-gray-400 hover:bg-gray-100 hover:text-gray-600 transition-colors"
        >
          <X className="h-4 w-4" />
        </button>
      </div>

      {/* Body */}
      <div className="flex-1 overflow-y-auto px-6 py-5 space-y-6">
        {/* Status badges */}
        <div className="flex flex-wrap items-center gap-2">
          <ResourceTypeBadge type={resource.type} />
          <StockStatusBadge current={resource.currentStock} threshold={resource.threshold} />
        </div>

        {/* Stock visual */}
        <StockProgress current={resource.currentStock} threshold={resource.threshold} />

        {/* Basic info */}
        <section>
          <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
            <Tag className="h-3.5 w-3.5" /> Identity
          </h3>
          <div className="grid grid-cols-2 gap-4">
            <DetailRow label="Name" value={resource.name} />
            <DetailRow label="Code" value={resource.resourceCode} />
            <DetailRow label="Type" value={resource.type} />
            <DetailRow label="Unit" value={resource.unit} />
          </div>
        </section>

        {/* Location */}
        <section>
          <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
            <MapPin className="h-3.5 w-3.5" /> Location
          </h3>
          <DetailRow label="Location" value={resource.location} />
        </section>

        {/* Stock numbers */}
        <section>
          <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
            <BarChart3 className="h-3.5 w-3.5" /> Stock Details
          </h3>
          <div className="grid grid-cols-2 gap-4">
            <DetailRow label="Current Stock" value={`${resource.currentStock} ${resource.unit}`} />
            <DetailRow label="Threshold" value={`${resource.threshold} ${resource.unit}`} />
          </div>
        </section>

        {/* Description */}
        {resource.description && (
          <section>
            <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
              <Package className="h-3.5 w-3.5" /> Description
            </h3>
            <p className="text-sm text-gray-700">{resource.description}</p>
          </section>
        )}

        {/* Timeline */}
        <section>
          <h3 className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-wide text-gray-500">
            <Calendar className="h-3.5 w-3.5" /> Timeline
          </h3>
          <div className="space-y-3">
            <DetailRow label="Created At" value={formatDateTime(resource.createdAt)} />
            <DetailRow label="Updated At" value={formatDateTime(resource.updatedAt)} />
          </div>
        </section>
      </div>

      {/* Footer actions */}
      <div className="flex gap-2 border-t border-gray-100 px-6 py-4">
        <Button variant="outline" size="sm" onClick={() => onEditStock(resource)} className="flex-1">
          Update Stock
        </Button>
        <Button variant="secondary" size="sm" onClick={() => onEdit(resource)} className="flex-1">
          Edit
        </Button>
        <Button variant="danger" size="sm" onClick={() => onDelete(resource)}>
          Delete
        </Button>
      </div>
    </div>
  );
}
