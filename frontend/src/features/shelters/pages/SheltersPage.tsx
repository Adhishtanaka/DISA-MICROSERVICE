import React, { useState } from "react";
import { useShelters } from "../hooks/useShelters";
import { ShelterList } from "../components/ShelterList";
import { ShelterForm } from "../components/ShelterForm";

export function SheltersPage() {
  const [showForm, setShowForm] = useState(false);
  const [filterAvailable, setFilterAvailable] = useState(false);
  const { shelters, loading, error, refetch } = useShelters(filterAvailable);

  const handleCreateSuccess = () => {
    setShowForm(false);
    refetch();
  };

  const stats = {
    total: shelters.length,
    operational: shelters.filter((s) => s.status === "OPERATIONAL").length,
    full: shelters.filter((s) => s.status === "FULL").length,
    totalCapacity: shelters.reduce((sum, s) => sum + s.totalCapacity, 0),
    totalOccupancy: shelters.reduce((sum, s) => sum + s.currentOccupancy, 0)
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">
            Shelter Management
          </h1>
          <p className="text-gray-600">
            Manage evacuation shelters and track occupancy
          </p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4 mb-8">
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">Total Shelters</div>
            <div className="text-2xl font-bold text-gray-900">{stats.total}</div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">Operational</div>
            <div className="text-2xl font-bold text-green-600">{stats.operational}</div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">Full</div>
            <div className="text-2xl font-bold text-red-600">{stats.full}</div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">Total Capacity</div>
            <div className="text-2xl font-bold text-blue-600">{stats.totalCapacity}</div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="text-sm text-gray-500 mb-1">Current Occupancy</div>
            <div className="text-2xl font-bold text-purple-600">{stats.totalOccupancy}</div>
          </div>
        </div>

        {/* Actions Bar */}
        <div className="bg-white rounded-lg shadow p-4 mb-6 flex flex-wrap gap-4 items-center justify-between">
          <div className="flex gap-3">
            <button
              onClick={() => setShowForm(true)}
              className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors"
            >
              + Create Shelter
            </button>
            <button
              onClick={refetch}
              disabled={loading}
              className="bg-gray-200 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-300 transition-colors disabled:opacity-50"
            >
              🔄 Refresh
            </button>
          </div>

          <div className="flex items-center gap-2">
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                checked={filterAvailable}
                onChange={(e) => setFilterAvailable(e.target.checked)}
                className="w-4 h-4 text-blue-600 rounded focus:ring-2 focus:ring-blue-500"
              />
              <span className="text-sm text-gray-700">Show Available Only</span>
            </label>
          </div>
        </div>

        {/* Error Message */}
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
            <p className="text-red-700">{error}</p>
          </div>
        )}

        {/* Loading State */}
        {loading && (
          <div className="text-center py-12">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            <p className="mt-4 text-gray-600">Loading shelters...</p>
          </div>
        )}

        {/* Shelter List */}
        {!loading && <ShelterList shelters={shelters} onRefresh={refetch} />}

        {/* Create Form Modal */}
        {showForm && (
          <ShelterForm
            onSuccess={handleCreateSuccess}
            onCancel={() => setShowForm(false)}
          />
        )}
      </div>
    </div>
  );
}
