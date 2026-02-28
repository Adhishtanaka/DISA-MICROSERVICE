import React, { useState } from "react";
import { Shelter } from "../types/shelter.types";
import { shelterApi } from "../api/shelterApi";

interface ShelterDetailProps {
  shelter: Shelter;
  onClose: () => void;
  onUpdate?: () => void;
}

export function ShelterDetail({ shelter, onClose, onUpdate }: ShelterDetailProps) {
  const [numberOfPeople, setNumberOfPeople] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const availableCapacity = shelter.totalCapacity - shelter.currentOccupancy;
  const occupancyPercentage = (shelter.currentOccupancy / shelter.totalCapacity) * 100;

  const handleCheckIn = async () => {
    if (numberOfPeople > availableCapacity) {
      setError(`Only ${availableCapacity} spaces available`);
      return;
    }

    try {
      setLoading(true);
      setError(null);
      await shelterApi.checkIn(shelter.id, { numberOfPeople });
      onUpdate?.();
      onClose();
    } catch (err: any) {
      setError(err.response?.data?.message || "Failed to check in");
    } finally {
      setLoading(false);
    }
  };

  const handleCheckOut = async () => {
    if (numberOfPeople > shelter.currentOccupancy) {
      setError(`Only ${shelter.currentOccupancy} people currently checked in`);
      return;
    }

    try {
      setLoading(true);
      setError(null);
      await shelterApi.checkOut(shelter.id, { numberOfPeople });
      onUpdate?.();
      onClose();
    } catch (err: any) {
      setError(err.response?.data?.message || "Failed to check out");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="p-6">
          <div className="flex justify-between items-start mb-6">
            <div>
              <h2 className="text-2xl font-bold text-gray-900">{shelter.name}</h2>
              <p className="text-sm text-gray-500">{shelter.shelterCode}</p>
            </div>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 text-2xl"
            >
              ×
            </button>
          </div>

          <div className="space-y-4">
            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="font-semibold text-gray-900 mb-2">Status</h3>
              <span
                className={`px-3 py-1 rounded-full text-sm font-medium ${
                  shelter.status === "OPERATIONAL"
                    ? "bg-green-100 text-green-800"
                    : shelter.status === "FULL"
                    ? "bg-red-100 text-red-800"
                    : shelter.status === "UNDER_PREPARATION"
                    ? "bg-yellow-100 text-yellow-800"
                    : "bg-gray-100 text-gray-800"
                }`}
              >
                {shelter.status.replace("_", " ")}
              </span>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="font-semibold text-gray-900 mb-2">Location</h3>
              <p className="text-gray-700">{shelter.address}</p>
              <p className="text-sm text-gray-500 mt-1">
                Coordinates: {shelter.latitude.toFixed(4)}, {shelter.longitude.toFixed(4)}
              </p>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="font-semibold text-gray-900 mb-2">Contact Information</h3>
              <p className="text-gray-700">{shelter.contactPerson}</p>
              <p className="text-gray-600">{shelter.contactNumber}</p>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="font-semibold text-gray-900 mb-3">Capacity</h3>
              <div className="space-y-2">
                <div className="flex justify-between text-sm">
                  <span>Current Occupancy:</span>
                  <span className="font-medium">{shelter.currentOccupancy}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span>Total Capacity:</span>
                  <span className="font-medium">{shelter.totalCapacity}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span>Available:</span>
                  <span className="font-medium text-green-600">{availableCapacity}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-3 mt-2">
                  <div
                    className={`h-3 rounded-full ${
                      occupancyPercentage >= 90
                        ? "bg-red-500"
                        : occupancyPercentage >= 70
                        ? "bg-yellow-500"
                        : "bg-green-500"
                    }`}
                    style={{ width: `${occupancyPercentage}%` }}
                  />
                </div>
                <p className="text-xs text-gray-500 text-center">
                  {occupancyPercentage.toFixed(1)}% occupied
                </p>
              </div>
            </div>

            {shelter.facilities && (
              <div className="bg-gray-50 p-4 rounded-lg">
                <h3 className="font-semibold text-gray-900 mb-2">Facilities</h3>
                <p className="text-gray-700">{shelter.facilities}</p>
              </div>
            )}

            {shelter.status === "OPERATIONAL" && (
              <div className="bg-blue-50 p-4 rounded-lg border border-blue-200">
                <h3 className="font-semibold text-gray-900 mb-3">Check In / Check Out</h3>
                
                {error && (
                  <div className="mb-3 p-3 bg-red-50 border border-red-200 rounded text-red-700 text-sm">
                    {error}
                  </div>
                )}

                <div className="mb-4">
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Number of People
                  </label>
                  <input
                    type="number"
                    min="1"
                    value={numberOfPeople}
                    onChange={(e) => setNumberOfPeople(parseInt(e.target.value) || 1)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div className="flex gap-3">
                  <button
                    onClick={handleCheckIn}
                    disabled={loading || availableCapacity === 0}
                    className="flex-1 bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                  >
                    {loading ? "Processing..." : "Check In"}
                  </button>
                  <button
                    onClick={handleCheckOut}
                    disabled={loading || shelter.currentOccupancy === 0}
                    className="flex-1 bg-orange-600 text-white py-2 px-4 rounded-md hover:bg-orange-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                  >
                    {loading ? "Processing..." : "Check Out"}
                  </button>
                </div>
              </div>
            )}

            <div className="text-xs text-gray-500 pt-4 border-t">
              <p>Created: {new Date(shelter.createdAt).toLocaleString()}</p>
              <p>Updated: {new Date(shelter.updatedAt).toLocaleString()}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
