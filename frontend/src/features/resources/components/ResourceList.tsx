import { useState } from 'react';
import { Package } from 'lucide-react';
import { ResourceCard } from './ResourceCard';
import { ResourceDetailPanel } from './ResourceDetailPanel';
import { StockUpdateModal } from './StockUpdateModal';
import { ResourceForm } from './ResourceForm';
import { DeleteConfirmModal } from './DeleteConfirmModal';
import { Modal } from '../../../components/ui/Modal';
import { Spinner } from '../../../components/ui/Spinner';
import type { Resource } from '../types/resource.types';

interface ResourceListProps {
  resources: Resource[];
  isLoading: boolean;
  error: string | null;
}

export function ResourceList({ resources, isLoading, error }: ResourceListProps) {
  const [detailResource, setDetailResource]   = useState<Resource | null>(null);
  const [stockResource, setStockResource]     = useState<Resource | null>(null);
  const [editResource, setEditResource]       = useState<Resource | null>(null);
  const [deleteResource, setDeleteResource]   = useState<Resource | null>(null);

  if (isLoading)
    return (
      <div className="flex flex-col items-center justify-center gap-3 py-20 text-gray-500">
        <Spinner size="lg" />
        <p className="text-sm">Loading resources…</p>
      </div>
    );

  if (error)
    return (
      <div className="flex flex-col items-center justify-center gap-2 py-20 text-center">
        <p className="font-medium text-red-600">Failed to load resources</p>
        <p className="text-sm text-gray-500">{error}</p>
      </div>
    );

  if (resources.length === 0)
    return (
      <div className="flex flex-col items-center justify-center gap-3 py-20 text-gray-400">
        <Package className="h-12 w-12 opacity-30" />
        <p className="text-sm">No resources found. Try adjusting your filters.</p>
      </div>
    );

  return (
    <>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
        {resources.map((resource) => (
          <ResourceCard
            key={resource.id}
            resource={resource}
            onViewDetails={setDetailResource}
            onEditStock={setStockResource}
            onEdit={setEditResource}
            onDelete={setDeleteResource}
          />
        ))}
      </div>

      {/* Slide-in detail panel */}
      {detailResource && (
        <>
          <div
            className="fixed inset-0 z-30 bg-black/20"
            onClick={() => setDetailResource(null)}
          />
          <ResourceDetailPanel
            resource={detailResource}
            onClose={() => setDetailResource(null)}
            onEditStock={(r) => { setDetailResource(null); setStockResource(r); }}
            onEdit={(r) => { setDetailResource(null); setEditResource(r); }}
            onDelete={(r) => { setDetailResource(null); setDeleteResource(r); }}
          />
        </>
      )}

      {/* Stock modal */}
      <StockUpdateModal resource={stockResource} onClose={() => setStockResource(null)} />

      {/* Edit modal */}
      <Modal
        isOpen={!!editResource}
        onClose={() => setEditResource(null)}
        title="Edit Resource"
        size="lg"
      >
        {editResource && (
          <ResourceForm
            resource={editResource}
            onSuccess={() => setEditResource(null)}
            onCancel={() => setEditResource(null)}
          />
        )}
      </Modal>

      {/* Delete confirm */}
      <DeleteConfirmModal resource={deleteResource} onClose={() => setDeleteResource(null)} />
    </>
  );
}
