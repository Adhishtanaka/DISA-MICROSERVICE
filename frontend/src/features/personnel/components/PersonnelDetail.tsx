import { useEffect, useState } from 'react';
import type { Person, AssignmentHistory } from '../types/personnel.types';

interface PersonnelDetailProps {
  person: Person;
  onEdit: () => void;
  onDelete: () => void;
  canManage: boolean;
  onGetAssignmentHistory: (personId: number) => Promise<AssignmentHistory[]>;
  onCompleteAssignment: (assignmentId: number) => Promise<void>;
}

export function PersonnelDetail({
  person,
  onEdit,
  onDelete,
  canManage,
  onGetAssignmentHistory,
  onCompleteAssignment,
}: PersonnelDetailProps) {
  const [assignmentHistory, setAssignmentHistory] = useState<AssignmentHistory[]>([]);
  const [loadingHistory, setLoadingHistory] = useState(false);

  useEffect(() => {
    let cancelled = false;
    setLoadingHistory(true);
    onGetAssignmentHistory(person.id)
      .then((data) => { if (!cancelled) setAssignmentHistory(data); })
      .catch(() => { if (!cancelled) setAssignmentHistory([]); })
      .finally(() => { if (!cancelled) setLoadingHistory(false); });
    return () => { cancelled = true; };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [person.id]);

  const handleDelete = () => {
    if (window.confirm(`Delete ${person.firstName} ${person.lastName}?`)) {
      onDelete();
    }
  };

  const handleComplete = async (assignmentId: number) => {
    await onCompleteAssignment(assignmentId);
    // Refresh history
    const updated = await onGetAssignmentHistory(person.id);
    setAssignmentHistory(updated);
  };

  const activeAssignments = assignmentHistory.filter((a) => a.status === 'ACTIVE');
  const completedAssignments = assignmentHistory.filter((a) => a.status === 'COMPLETED');

  return (
    <div className="bg-white rounded-lg border border-gray-200 p-5 space-y-4 sticky top-4">
      {/* Header */}
      <div>
        <h3 className="text-lg font-bold text-gray-900">
          {person.firstName} {person.lastName}
        </h3>
        {person.personalCode && (
          <p className="text-sm font-mono text-gray-500">{person.personalCode}</p>
        )}
      </div>

      {canManage && (
        <div className="flex gap-2">
          <button
            onClick={onEdit}
            className="flex-1 px-3 py-1.5 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700 transition-colors"
          >
            Edit
          </button>
          <button
            onClick={handleDelete}
            className="px-3 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition-colors"
          >
            Delete
          </button>
        </div>
      )}

      {/* Contact */}
      <Section title="Contact">
        <Detail label="Phone" value={person.phone} />
        <Detail label="Email" value={person.email} />
        <Detail label="Address" value={person.address} />
      </Section>

      {/* Organization */}
      <Section title="Organization">
        <Detail label="Role" value={person.role} />
        <Detail label="Department" value={person.department} />
        <Detail label="Organization" value={person.organization} />
        <Detail label="Rank" value={person.rank} />
        <Detail label="Status" value={person.status} />
      </Section>

      {/* Assignments */}
      <Section title="Assignments">
        {loadingHistory ? (
          <p className="text-xs text-gray-400">Loading assignments...</p>
        ) : assignmentHistory.length === 0 ? (
          <p className="text-xs text-gray-400">No assignments</p>
        ) : (
          <div className="space-y-2">
            {activeAssignments.length > 0 && (
              <div>
                <p className="text-xs font-medium text-green-700 mb-1">Active ({activeAssignments.length})</p>
                {activeAssignments.map((a) => (
                  <div key={a.id} className="bg-green-50 border border-green-100 rounded p-2 mb-1">
                    <div className="flex items-center justify-between">
                      <div>
                        <span className="font-mono text-xs text-green-700">{a.taskCode}</span>
                        <span className="text-xs text-gray-700 ml-1">{a.taskTitle}</span>
                      </div>
                      <span className="text-xs bg-green-100 text-green-800 px-1.5 py-0.5 rounded">
                        {a.matchScore.toFixed(0)}%
                      </span>
                    </div>
                    <p className="text-xs text-gray-500 mt-0.5">{a.taskType} &middot; {new Date(a.assignedAt).toLocaleDateString()}</p>
                    {canManage && (
                      <button
                        onClick={() => handleComplete(a.id)}
                        className="mt-1 text-xs px-2 py-0.5 bg-green-600 text-white rounded hover:bg-green-700 transition-colors"
                      >
                        Complete
                      </button>
                    )}
                  </div>
                ))}
              </div>
            )}
            {completedAssignments.length > 0 && (
              <div>
                <p className="text-xs font-medium text-gray-500 mb-1">Completed ({completedAssignments.length})</p>
                {completedAssignments.map((a) => (
                  <div key={a.id} className="bg-gray-50 border border-gray-100 rounded p-2 mb-1">
                    <div className="flex items-center justify-between">
                      <div>
                        <span className="font-mono text-xs text-gray-500">{a.taskCode}</span>
                        <span className="text-xs text-gray-600 ml-1">{a.taskTitle}</span>
                      </div>
                      <span className="text-xs bg-gray-100 text-gray-600 px-1.5 py-0.5 rounded">
                        {a.matchScore.toFixed(0)}%
                      </span>
                    </div>
                    <p className="text-xs text-gray-400 mt-0.5">
                      {a.taskType} &middot; {new Date(a.assignedAt).toLocaleDateString()}
                      {a.completedAt && <> &rarr; {new Date(a.completedAt).toLocaleDateString()}</>}
                    </p>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </Section>

      {/* Shift */}
      {(person.shiftStartTime || person.shiftEndTime) && (
        <Section title="Shift">
          <Detail label="Start" value={person.shiftStartTime ? new Date(person.shiftStartTime).toLocaleString() : '-'} />
          <Detail label="End" value={person.shiftEndTime ? new Date(person.shiftEndTime).toLocaleString() : '-'} />
        </Section>
      )}

      {/* Skills */}
      {person.skills?.length > 0 && (
        <Section title="Skills">
          {person.skills.map((s, i) => (
            <div key={i} className="flex items-center justify-between text-sm py-1">
              <span className="font-medium text-gray-700">{s.profession}</span>
              <span className="text-xs text-gray-500">
                {s.level} &middot; {s.experienceYears}yr &middot; {s.missionCount} missions
              </span>
            </div>
          ))}
        </Section>
      )}

      {/* Emergency Contacts */}
      {person.emergencyContacts?.length > 0 && (
        <Section title="Emergency Contacts">
          {person.emergencyContacts.map((c, i) => (
            <div key={i} className="text-sm py-1 border-b border-gray-50 last:border-0">
              <p className="font-medium text-gray-700">{c.name} ({c.relation})</p>
              <p className="text-gray-500 text-xs">{c.telephone}</p>
            </div>
          ))}
        </Section>
      )}

      {/* Medical */}
      {person.medicalCondition && (
        <Section title="Medical">
          <Detail label="Blood Group" value={person.medicalCondition.bloodGroup} />
          <Detail label="Height" value={person.medicalCondition.height} />
          <Detail label="Weight" value={person.medicalCondition.weight} />
          {person.medicalCondition.allergies && person.medicalCondition.allergies.length > 0 && (
            <div className="mt-1">
              <span className="text-xs font-medium text-gray-500">Allergies:</span>
              <div className="flex flex-wrap gap-1 mt-0.5">
                {person.medicalCondition.allergies.map((a, i) => (
                  <span key={i} className="text-xs bg-red-50 text-red-600 px-1.5 py-0.5 rounded">
                    {a.allergyTo} ({a.type})
                  </span>
                ))}
              </div>
            </div>
          )}
          {person.medicalCondition.chronicConditions && person.medicalCondition.chronicConditions.length > 0 && (
            <div className="mt-1">
              <span className="text-xs font-medium text-gray-500">Chronic Conditions:</span>
              <div className="flex flex-wrap gap-1 mt-0.5">
                {person.medicalCondition.chronicConditions.map((c, i) => (
                  <span key={i} className="text-xs bg-yellow-50 text-yellow-700 px-1.5 py-0.5 rounded">
                    {c.name} ({c.severity})
                  </span>
                ))}
              </div>
            </div>
          )}
          {person.medicalCondition.physicalLimitations && person.medicalCondition.physicalLimitations.length > 0 && (
            <div className="mt-1">
              <span className="text-xs font-medium text-gray-500">Physical Limitations:</span>
              <div className="flex flex-wrap gap-1 mt-0.5">
                {person.medicalCondition.physicalLimitations.map((l, i) => (
                  <span key={i} className="text-xs bg-orange-50 text-orange-600 px-1.5 py-0.5 rounded">
                    {l.limitation}
                  </span>
                ))}
              </div>
            </div>
          )}
        </Section>
      )}

      {/* Metadata */}
      <div className="text-xs text-gray-400 pt-2 border-t border-gray-100">
        <p>Created: {new Date(person.createdAt).toLocaleString()}</p>
        <p>Updated: {new Date(person.updatedAt).toLocaleString()}</p>
      </div>
    </div>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div>
      <h4 className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-1">
        {title}
      </h4>
      <div className="space-y-0.5">{children}</div>
    </div>
  );
}

function Detail({ label, value }: { label: string; value?: string | null }) {
  if (!value) return null;
  return (
    <div className="flex justify-between text-sm">
      <span className="text-gray-500">{label}</span>
      <span className="text-gray-800 font-medium text-right">{value}</span>
    </div>
  );
}
