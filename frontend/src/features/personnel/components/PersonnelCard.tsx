import type { Person, AssignmentHistory } from '../types/personnel.types';

interface PersonnelCardProps {
  person: Person;
  isSelected: boolean;
  onClick: () => void;
  activeAssignments?: AssignmentHistory[];
}

const statusColor: Record<string, string> = {
  Available: 'bg-green-100 text-green-800',
  'On Duty': 'bg-blue-100 text-blue-800',
  'On Leave': 'bg-yellow-100 text-yellow-800',
  Inactive: 'bg-gray-100 text-gray-800',
};

export function PersonnelCard({ person, isSelected, onClick, activeAssignments }: PersonnelCardProps) {
  return (
    <div
      onClick={onClick}
      className={`p-4 rounded-lg border cursor-pointer transition-all hover:shadow-md ${
        isSelected
          ? 'border-blue-500 bg-blue-50 ring-2 ring-blue-200'
          : 'border-gray-200 bg-white hover:border-gray-300'
      }`}
    >
      <div className="flex items-start justify-between mb-2">
        <div>
          <h3 className="font-semibold text-gray-900">
            {person.firstName} {person.lastName}
          </h3>
          {person.personalCode && (
            <span className="text-xs font-mono text-gray-500">{person.personalCode}</span>
          )}
        </div>
        <span
          className={`text-xs font-medium px-2 py-0.5 rounded-full ${statusColor[person.status] ?? 'bg-gray-100 text-gray-700'}`}
        >
          {person.status}
        </span>
      </div>

      <div className="text-sm text-gray-600 space-y-1">
        <p>{person.role} &middot; {person.department}</p>
        <p className="text-xs text-gray-500">{person.organization}</p>
      </div>

      {activeAssignments && activeAssignments.length > 0 && (
        <div className="mt-2 space-y-1">
          {activeAssignments.map((a) => (
            <div key={a.id} className="flex items-center gap-1.5 bg-blue-50 border border-blue-100 rounded px-2 py-1">
              <span className="text-xs font-mono text-blue-600">{a.taskCode}</span>
              <span className="text-xs text-blue-800 truncate">{a.taskTitle}</span>
            </div>
          ))}
        </div>
      )}

      {person.skills?.length > 0 && (
        <div className="flex flex-wrap gap-1 mt-2">
          {person.skills.slice(0, 3).map((s, i) => (
            <span key={i} className="text-xs bg-purple-50 text-purple-700 px-1.5 py-0.5 rounded">
              {s.profession}
            </span>
          ))}
          {person.skills.length > 3 && (
            <span className="text-xs text-gray-400">+{person.skills.length - 3} more</span>
          )}
        </div>
      )}

      <div className="text-xs text-gray-400 mt-2">
        {person.phone} &middot; {person.email}
      </div>
    </div>
  );
}
