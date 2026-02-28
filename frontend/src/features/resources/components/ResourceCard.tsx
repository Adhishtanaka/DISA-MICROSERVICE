import { MapPin, Package, ChevronRight, TrendingUp, TrendingDown } from 'lucide-react';
import { Card, CardBody } from '../../../components/ui/Card';
import { Button } from '../../../components/ui/Button';
import { ResourceTypeBadge, StockStatusBadge } from './ResourceBadges';
import type { Resource } from '../types/resource.types';

interface ResourceCardProps {
  resource: Resource;
  onViewDetails: (resource: Resource) => void;
  onEditStock: (resource: Resource) => void;
  onEdit: (resource: Resource) => void;
  onDelete: (resource: Resource) => void;
}

function StockBar({ current, threshold }: { current: number; threshold: number }) {
  const max = Math.max(current, threshold) * 1.5;
  const pct = Math.min((current / max) * 100, 100);
  const isLow = current <= threshold;
  return (
    <div className="mt-2">
      <div className="flex items-center justify-between text-xs text-gray-500 mb-1">
        <span>Stock</span>
        <span className={isLow ? 'font-semibold text-red-600' : 'text-gray-700'}>
          {current} / {threshold} threshold
        </span>
      </div>
      <div className="h-1.5 w-full overflow-hidden rounded-full bg-gray-100">
        <div
          className={`h-full rounded-full transition-all ${isLow ? 'bg-red-500' : 'bg-green-500'}`}
          style={{ width: `${pct}%` }}
        />
      </div>
    </div>
  );
}

function formatDate(dateStr: string): string {
  return new Date(dateStr).toLocaleDateString('en-US', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  });
}

export function ResourceCard({ resource, onViewDetails, onEditStock, onEdit, onDelete }: ResourceCardProps) {
  const isLow = resource.currentStock <= resource.threshold;

  return (
    <Card className={`transition-shadow hover:shadow-md ${isLow ? 'ring-1 ring-red-200' : ''}`}>
      <CardBody>
        {/* Header */}
        <div className="flex items-start justify-between gap-3">
          <div className="min-w-0">
            <p className="text-xs font-mono text-gray-400 truncate">{resource.resourceCode}</p>
            <h3 className="mt-0.5 text-sm font-semibold text-gray-900 truncate">{resource.name}</h3>
          </div>
          <StockStatusBadge current={resource.currentStock} threshold={resource.threshold} />
        </div>

        {/* Type + location */}
        <div className="mt-3 flex flex-wrap items-center gap-2">
          <ResourceTypeBadge type={resource.type} />
          <span className="inline-flex items-center gap-1 text-xs text-gray-500">
            <MapPin className="h-3 w-3 text-blue-400" />
            {resource.location}
          </span>
        </div>

        {/* Stock bar */}
        <StockBar current={resource.currentStock} threshold={resource.threshold} />

        {/* Current stock pill */}
        <div className="mt-2 flex items-center gap-1.5">
          <Package className="h-3.5 w-3.5 text-gray-400" />
          <span className="text-xs text-gray-600">
            <span className={`font-semibold ${isLow ? 'text-red-600' : 'text-gray-900'}`}>
              {resource.currentStock}
            </span>{' '}
            {resource.unit}
          </span>
          {isLow ? (
            <TrendingDown className="h-3.5 w-3.5 text-red-500" />
          ) : (
            <TrendingUp className="h-3.5 w-3.5 text-green-500" />
          )}
        </div>

        {resource.description && (
          <p className="mt-2 line-clamp-2 text-xs text-gray-500">{resource.description}</p>
        )}

        <p className="mt-2 text-xs text-gray-400">
          Updated {formatDate(resource.updatedAt)}
        </p>

        {/* Actions */}
        <div className="mt-4 grid grid-cols-4 gap-1.5 border-t border-gray-100 pt-3">
          <Button variant="ghost" size="sm" onClick={() => onViewDetails(resource)} className="col-span-1">
            <ChevronRight className="h-4 w-4" />
          </Button>
          <Button variant="outline" size="sm" onClick={() => onEditStock(resource)} className="col-span-1 text-xs">
            Stock
          </Button>
          <Button variant="secondary" size="sm" onClick={() => onEdit(resource)} className="col-span-1 text-xs">
            Edit
          </Button>
          <Button variant="danger" size="sm" onClick={() => onDelete(resource)} className="col-span-1 text-xs">
            Del
          </Button>
        </div>
      </CardBody>
    </Card>
  );
}
