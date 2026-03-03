import { useState } from 'react';
import { usePersonnel } from '../hooks/usePersonnel';
import { PersonnelList } from '../components/PersonnelList';
import { PersonnelForm } from '../components/PersonnelForm';
import { useRole } from '../../auth';
import type { Person, PersonRequest } from '../types/personnel.types';

export function PersonnelPage() {
  const {
    personnel,
    loading,
    error,
    createPerson,
    updatePerson,
    deletePerson,
    getPendingTasks,
    matchAllPending,
    matchTask,
    getActiveAssignments,
    getAssignmentHistory,
    completeAssignment,
    refetch,
  } = usePersonnel();

  const { canCreate, canDelete } = useRole();
  const [showForm, setShowForm] = useState(false);
  const [editPerson, setEditPerson] = useState<Person | null>(null);

  const handleCreate = async (data: PersonRequest) => {
    await createPerson(data);
    setShowForm(false);
  };

  const handleUpdate = async (data: PersonRequest) => {
    await updatePerson(data);
    setEditPerson(null);
  };

  const handleDelete = async (id: number) => {
    await deletePerson(id);
    setEditPerson(null);
  };

  const handleEdit = (person: Person) => {
    setEditPerson(person);
  };

  const stats = {
    total: personnel.filter((p) => !p.disabled).length,
    available: personnel.filter((p) => p.status === 'Available' && !p.disabled).length,
    onDuty: personnel.filter((p) => p.status === 'On Duty' && !p.disabled).length,
    onLeave: personnel.filter((p) => p.status === 'On Leave' && !p.disabled).length,
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Personnel Management</h1>
          <p className="text-gray-600">
            Manage disaster response personnel, skills, and AI-powered task assignments
          </p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">Total Personnel</div>
            <div className="text-2xl font-bold text-gray-900">{stats.total}</div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">Available</div>
            <div className="text-2xl font-bold text-green-600">{stats.available}</div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">On Duty</div>
            <div className="text-2xl font-bold text-blue-600">{stats.onDuty}</div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">On Leave</div>
            <div className="text-2xl font-bold text-yellow-600">{stats.onLeave}</div>
          </div>
        </div>

        {/* Actions Bar */}
        <div className="bg-white rounded-lg shadow p-4 mb-6 flex flex-wrap gap-4 items-center justify-between">
          <div className="flex gap-3">
            {canCreate && (
              <button
                onClick={() => setShowForm(true)}
                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors"
              >
                + Add Personnel
              </button>
            )}
            <button
              onClick={refetch}
              disabled={loading}
              className="bg-gray-200 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-300 transition-colors disabled:opacity-50"
            >
              Refresh
            </button>
          </div>
          <div className="text-sm text-gray-500">
            {stats.total} personnel registered
          </div>
        </div>

        {/* Error */}
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
            <p className="text-red-700">{error}</p>
          </div>
        )}

        {/* Loading — only on initial load, not during refresh */}
        {loading && personnel.length === 0 && (
          <div className="text-center py-12">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            <p className="mt-4 text-gray-600">Loading personnel...</p>
          </div>
        )}

        {/* Personnel List — stays mounted during refresh so Gemini state is preserved */}
        {personnel.length > 0 && (
          <PersonnelList
            personnel={personnel}
            onEdit={handleEdit}
            onDelete={handleDelete}
            onMatchTask={matchTask}
            onMatchAllPending={matchAllPending}
            onGetPendingTasks={getPendingTasks}
            onGetActiveAssignments={getActiveAssignments}
            onGetAssignmentHistory={getAssignmentHistory}
            onCompleteAssignment={completeAssignment}
            onRefresh={refetch}
          />
        )}

        {/* Create Form Modal */}
        {showForm && (
          <PersonnelForm
            onSubmit={handleCreate}
            onCancel={() => setShowForm(false)}
          />
        )}

        {/* Edit Form Modal */}
        {editPerson && (
          <PersonnelForm
            person={editPerson}
            onSubmit={handleUpdate}
            onCancel={() => setEditPerson(null)}
            onDelete={canDelete ? handleDelete : undefined}
          />
        )}
      </div>
    </div>
  );
}
