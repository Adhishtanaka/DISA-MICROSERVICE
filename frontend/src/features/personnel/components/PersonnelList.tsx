import { useCallback, useEffect, useRef, useState } from 'react';
import type { Person, TaskDto, TaskAssignment, AssignmentHistory } from '../types/personnel.types';
import { PersonnelCard } from './PersonnelCard';
import { PersonnelDetail } from './PersonnelDetail';
import { useRole } from '../../auth';

interface PersonnelListProps {
  personnel: Person[];
  onEdit: (person: Person) => void;
  onDelete: (id: number) => Promise<void>;
  onMatchTask: (task: TaskDto) => Promise<TaskAssignment>;
  onMatchAllPending: () => Promise<TaskAssignment[]>;
  onGetPendingTasks: () => Promise<TaskDto[]>;
  onGetActiveAssignments: (personId: number) => Promise<AssignmentHistory[]>;
  onGetAssignmentHistory: (personId: number) => Promise<AssignmentHistory[]>;
  onCompleteAssignment: (assignmentId: number) => Promise<AssignmentHistory>;
  onRefresh: () => void | Promise<void>;
}

export function PersonnelList({
  personnel,
  onEdit,
  onDelete,
  onMatchTask,
  onMatchAllPending,
  onGetPendingTasks,
  onGetActiveAssignments,
  onGetAssignmentHistory,
  onCompleteAssignment,
  onRefresh,
}: PersonnelListProps) {
  const [selected, setSelected] = useState<Person | null>(null);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const { canCreate } = useRole();

  // Gemini assignment state
  const [pendingTasks, setPendingTasks] = useState<TaskDto[]>([]);
  const [assignments, setAssignments] = useState<TaskAssignment[]>([]);
  const [assigning, setAssigning] = useState(false);
  const [assignError, setAssignError] = useState<string | null>(null);

  // Active assignments map: personId -> AssignmentHistory[]
  const [activeAssignmentsMap, setActiveAssignmentsMap] = useState<Record<number, AssignmentHistory[]>>({});

  // Use ref to avoid infinite loop — personnel changes trigger re-render but we only want to fetch on mount + explicit calls
  const personnelRef = useRef(personnel);
  personnelRef.current = personnel;
  const onGetActiveAssignmentsRef = useRef(onGetActiveAssignments);
  onGetActiveAssignmentsRef.current = onGetActiveAssignments;

  // Fetch active assignments for all "On Duty" personnel
  const fetchActiveAssignments = useCallback(async () => {
    const onDutyPersonnel = personnelRef.current.filter((p) => p.status === 'On Duty' && !p.disabled);
    if (onDutyPersonnel.length === 0) {
      setActiveAssignmentsMap({});
      return;
    }
    try {
      const results = await Promise.all(
        onDutyPersonnel.map(async (p) => {
          const active = await onGetActiveAssignmentsRef.current(p.id);
          return { personId: p.id, assignments: active };
        })
      );
      const map: Record<number, AssignmentHistory[]> = {};
      results.forEach((r) => { map[r.personId] = r.assignments; });
      setActiveAssignmentsMap(map);
    } catch {
      // Silently fail — assignments just won't show on cards
    }
  }, []);

  // Fetch active assignments when personnel list changes (e.g. after refresh)
  useEffect(() => {
    fetchActiveAssignments();
  }, [personnel, fetchActiveAssignments]);

  const filtered = personnel.filter((p) => {
    if (p.disabled) return false;
    const matchSearch =
      !search ||
      `${p.firstName} ${p.lastName} ${p.personalCode} ${p.role} ${p.department}`
        .toLowerCase()
        .includes(search.toLowerCase());
    const matchStatus = statusFilter === 'ALL' || p.status === statusFilter;
    return matchSearch && matchStatus;
  });

  const handleFetchPending = async () => {
    setAssignError(null);
    try {
      const tasks = await onGetPendingTasks();
      setPendingTasks(tasks);
    } catch {
      setAssignError('Failed to fetch pending tasks');
    }
  };

  const handleAssignAll = async () => {
    setAssigning(true);
    setAssignError(null);
    try {
      const result = await onMatchAllPending();
      setAssignments(result);
      setPendingTasks([]);
      // Refresh personnel list first, then fetch active assignments
      await onRefresh();
      await fetchActiveAssignments();
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Unknown error';
      setAssignError(`Gemini assignment failed: ${msg}`);
    } finally {
      setAssigning(false);
    }
  };

  const handleAssignSingle = async (task: TaskDto) => {
    setAssigning(true);
    setAssignError(null);
    try {
      const result = await onMatchTask(task);
      setAssignments((prev) => [...prev, result]);
      setPendingTasks((prev) => prev.filter((t) => t.id !== task.id));
      await onRefresh();
      await fetchActiveAssignments();
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Unknown error';
      setAssignError(`Gemini assignment failed: ${msg}`);
    } finally {
      setAssigning(false);
    }
  };

  const handleCompleteAssignment = async (assignmentId: number) => {
    try {
      await onCompleteAssignment(assignmentId);
      await onRefresh();
      await fetchActiveAssignments();
    } catch {
      // handled in detail
    }
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
      {/* Left: Personnel List */}
      <div className="lg:col-span-2 space-y-4">
        {/* Search & Filter */}
        <div className="flex flex-wrap gap-3">
          <input
            type="text"
            placeholder="Search personnel..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="flex-1 min-w-[200px] px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="ALL">All Statuses</option>
            <option value="Available">Available</option>
            <option value="On Duty">On Duty</option>
            <option value="On Leave">On Leave</option>
            <option value="Inactive">Inactive</option>
          </select>
        </div>

        {/* Gemini Task Assignment Panel */}
        {canCreate && (
          <div className="bg-gradient-to-r from-indigo-50 to-purple-50 border border-indigo-200 rounded-lg p-4">
            <h3 className="text-sm font-semibold text-indigo-900 mb-2">
              Gemini AI Task Assignment
            </h3>
            <div className="flex flex-wrap gap-2 mb-3">
              <button
                onClick={handleFetchPending}
                disabled={assigning}
                className="px-3 py-1.5 bg-indigo-600 text-white text-sm rounded-md hover:bg-indigo-700 disabled:bg-gray-300 transition-colors"
              >
                Load Pending Tasks
              </button>
              <button
                onClick={handleAssignAll}
                disabled={assigning || pendingTasks.length === 0}
                className="px-3 py-1.5 bg-purple-600 text-white text-sm rounded-md hover:bg-purple-700 disabled:bg-gray-300 transition-colors"
              >
                Auto-Assign All via Gemini
              </button>
            </div>

            {/* Gemini loading indicator */}
            {assigning && (
              <div className="flex items-center gap-3 bg-indigo-100 border border-indigo-200 rounded-lg p-3 mb-3">
                <div className="animate-spin rounded-full h-5 w-5 border-2 border-indigo-300 border-t-indigo-600"></div>
                <div>
                  <p className="text-sm font-medium text-indigo-900">Gemini AI is assigning tasks...</p>
                  <p className="text-xs text-indigo-600">This may take a minute for multiple tasks. Please wait.</p>
                </div>
              </div>
            )}

            {assignError && (
              <div className="text-sm text-red-600 mb-2">{assignError}</div>
            )}

            {/* Pending tasks list */}
            {pendingTasks.length > 0 && (
              <div className="space-y-2 max-h-48 overflow-y-auto">
                <p className="text-xs text-gray-500 font-medium">
                  {pendingTasks.length} pending task(s):
                </p>
                {pendingTasks.map((task) => (
                  <div
                    key={task.id}
                    className="flex items-center justify-between bg-white rounded p-2 text-sm border border-gray-100"
                  >
                    <div>
                      <span className="font-mono text-xs text-gray-500 mr-2">
                        {task.taskCode}
                      </span>
                      <span className="font-medium">{task.title}</span>
                      <span className={`ml-2 text-xs px-1.5 py-0.5 rounded ${
                        task.priority === 'URGENT'
                          ? 'bg-red-100 text-red-700'
                          : task.priority === 'HIGH'
                            ? 'bg-orange-100 text-orange-700'
                            : 'bg-gray-100 text-gray-600'
                      }`}>
                        {task.priority}
                      </span>
                    </div>
                    <button
                      onClick={() => handleAssignSingle(task)}
                      disabled={assigning}
                      className="text-xs px-2 py-1 bg-indigo-100 text-indigo-700 rounded hover:bg-indigo-200 disabled:opacity-50 flex items-center gap-1"
                    >
                      {assigning && <span className="inline-block animate-spin h-3 w-3 border border-indigo-400 border-t-indigo-700 rounded-full"></span>}
                      Assign
                    </button>
                  </div>
                ))}
              </div>
            )}

            {/* Assignment results */}
            {assignments.length > 0 && (
              <div className="mt-3 space-y-2">
                <p className="text-xs text-gray-500 font-medium">
                  Recent Assignments:
                </p>
                {assignments.map((a, i) => (
                  <div
                    key={i}
                    className="bg-white rounded p-2 text-sm border border-green-100"
                  >
                    <div className="flex items-center justify-between">
                      <span>
                        <span className="font-medium">
                          {a.assignedPerson.firstName} {a.assignedPerson.lastName}
                        </span>
                        <span className="text-gray-400 mx-1">&rarr;</span>
                        <span className="font-mono text-xs">{a.task.taskCode}</span>
                        <span className="ml-1 text-gray-600">{a.task.title}</span>
                      </span>
                      <span className="text-xs bg-green-100 text-green-700 px-1.5 py-0.5 rounded">
                        {a.matchScore.toFixed(0)}% match
                      </span>
                    </div>
                    <p className="text-xs text-gray-500 mt-1">{a.assignmentReason}</p>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* Personnel Grid */}
        {filtered.length === 0 ? (
          <div className="text-center py-12 text-gray-500">
            <p className="text-lg font-medium">No personnel found</p>
            <p className="text-sm">Try adjusting your search or filter</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            {filtered.map((p) => (
              <PersonnelCard
                key={p.id}
                person={p}
                isSelected={selected?.id === p.id}
                onClick={() => setSelected(selected?.id === p.id ? null : p)}
                activeAssignments={activeAssignmentsMap[p.id]}
              />
            ))}
          </div>
        )}
      </div>

      {/* Right: Detail Panel */}
      <div className="lg:col-span-1">
        {selected ? (
          <PersonnelDetail
            person={selected}
            onEdit={() => onEdit(selected)}
            onDelete={() => onDelete(selected.id)}
            canManage={canCreate}
            onGetAssignmentHistory={onGetAssignmentHistory}
            onCompleteAssignment={handleCompleteAssignment}
          />
        ) : (
          <div className="bg-white rounded-lg border border-gray-200 p-6 text-center text-gray-400">
            <p className="text-sm">Select a person to view details</p>
          </div>
        )}
      </div>
    </div>
  );
}
