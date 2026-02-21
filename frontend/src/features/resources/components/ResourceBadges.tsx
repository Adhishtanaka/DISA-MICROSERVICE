import { Badge } from '../../../components/ui/Badge';
import type { ResourceType } from '../types/resource.types';

type TypeVariant = 'default' | 'pending' | 'in-progress' | 'completed' | 'cancelled' | 'blue' | 'purple';

const typeMap: Record<ResourceType, { label: string; variant: TypeVariant }> = {
  FOOD:      { label: 'Food',      variant: 'completed' },
  WATER:     { label: 'Water',     variant: 'blue' },
  MEDICINE:  { label: 'Medicine',  variant: 'cancelled' },
  EQUIPMENT: { label: 'Equipment', variant: 'default' },
  CLOTHING:  { label: 'Clothing',  variant: 'purple' },
  HYGIENE:   { label: 'Hygiene',   variant: 'pending' },
};

export function ResourceTypeBadge({ type }: { type: ResourceType }) {
  const { label, variant } = typeMap[type] ?? { label: type, variant: 'default' as TypeVariant };
  return <Badge variant={variant}>{label}</Badge>;
}

export function StockStatusBadge({ current, threshold }: { current: number; threshold: number }) {
  const isLow = current <= threshold;
  return (
    <Badge variant={isLow ? 'cancelled' : 'completed'}>
      {isLow ? 'Low Stock' : 'In Stock'}
    </Badge>
  );
}
