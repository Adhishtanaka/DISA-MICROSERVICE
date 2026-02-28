import { Badge } from '../../../components/ui/Badge';
import { Button } from '../../../components/ui/Button';
import { Card, CardBody } from '../../../components/ui/Card';
import type { Task } from '../types/task.types';

const priorityVariant: Record<string, 'blue' | 'default' | 'in-progress' | 'cancelled'> = {
  LOW: 'default',
  MEDIUM: 'blue',
  HIGH: 'in-progress',
  CRITICAL: 'cancelled',
};

const statusVariant: Record<string, 'pending' | 'in-progress' | 'completed'> = {
  PENDING: 'pending',
  IN_PROGRESS: 'in-progress',
  COMPLETED: 'completed',
};

const typeLabel: Record<string, string> = {
  RESCUE_OPERATION: 'Rescue',
  MEDICAL_AID: 'Medical Aid',
  DEBRIS_REMOVAL: 'Debris Removal',
  SUPPLY_DELIVERY: 'Supply Delivery',
  EVACUATION: 'Evacuation',
};

interface TaskCardProps {
  task: Task;
  onComplete: (id: number) => void;
  onDelete: (id: number) => void;
}

export function TaskCard({ task, onComplete, onDelete }: TaskCardProps) {
  return (
    <Card>
      <CardBody>
        <div className="flex items-start justify-between gap-2">
          <div className="min-w-0 flex-1">
            <div className="flex flex-wrap items-center gap-2">
              <span className="text-xs font-mono text-gray-400">{task.taskCode}</span>
              <Badge variant={statusVariant[task.status]}>{task.status.replace('_', ' ')}</Badge>
              <Badge variant={priorityVariant[task.priority]}>{task.priority}</Badge>
            </div>
            <h3 className="mt-1 truncate font-semibold text-gray-900">{task.title}</h3>
            <p className="mt-1 text-sm text-gray-500 line-clamp-2">{task.description}</p>

            <div className="mt-3 flex flex-wrap gap-4 text-xs text-gray-500">
              <span>
                <span className="font-medium text-gray-700">Type: </span>
                {typeLabel[task.type] ?? task.type}
              </span>
              {task.location && (
                <span>
                  <span className="font-medium text-gray-700">Location: </span>
                  {task.location}
                </span>
              )}
              {task.incidentId && (
                <span>
                  <span className="font-medium text-gray-700">Incident: </span>
                  #{task.incidentId}
                </span>
              )}
              {task.assignedTo && (
                <span>
                  <span className="font-medium text-gray-700">Assigned: </span>
                  #{task.assignedTo}
                </span>
              )}
            </div>
          </div>
        </div>

        <div className="mt-4 flex items-center justify-between">
          <span className="text-xs text-gray-400">
            {new Date(task.createdAt).toLocaleDateString()}
          </span>
          <div className="flex gap-2">
            {task.status !== 'COMPLETED' && (
              <Button size="sm" variant="secondary" onClick={() => onComplete(task.id)}>
                Complete
              </Button>
            )}
            <Button size="sm" variant="danger" onClick={() => onDelete(task.id)}>
              Delete
            </Button>
          </div>
        </div>
      </CardBody>
    </Card>
  );
}
