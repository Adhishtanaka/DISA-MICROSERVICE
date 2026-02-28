import React from "react";
import { Shelter } from "../types/shelter.types";

interface ShelterCardProps {
  shelter: Shelter;
  onClick?: () => void;
}

export function ShelterCard({ shelter, onClick }: ShelterCardProps) {
  const availableCapacity = shelter.totalCapacity - shelter.currentOccupancy;
  const occupancyPercentage = (shelter.currentOccupancy / shelter.totalCapacity) * 100;

  const getStatusColor = (status: string) => {
    switch (status) {
      case "OPERATIONAL":
        return "bg-green-100 text-green-800";
      case "FULL":
        return "bg-red-100 text-red-800";
      case "UNDER_PREPARATION":
        return "bg-yellow-100 text-yellow-800";
      case "CLOSED":
        return "bg-gray-100 text-gray-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const getOccupancyColor = (percentage: number) => {
    if (percentage >= 90) return "bg-red-500";
    if (percentage >= 70) return "bg-yellow-500";
    return "bg-green-500";
  };

  return (
    <div
      onClick={onClick}
      className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow cursor-pointer border border-gray-200"
    >
      <div className="flex justify-between items-start mb-4">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">{shelter.name}</h3>
          <p className="text-sm text-gray-500">{shelter.shelterCode}</p>
        </div>
        <span
          className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
            shelter.status
          )}`}
        >
          {shelter.status.replace("_", " ")}
        </span>
      </div>

      <div className="space-y-2 mb-4">
        <p className="text-sm text-gray-600">
          <span className="font-medium">📍 Address:</span> {shelter.address}
        </p>
        <p className="text-sm text-gray-600">
          <span className="font-medium">👤 Contact:</span> {shelter.contactPerson} ({shelter.contactNumber})
        </p>
      </div>

      <div className="mb-3">
        <div className="flex justify-between text-sm mb-1">
          <span className="text-gray-600">Occupancy</span>
          <span className="font-medium">
            {shelter.currentOccupancy} / {shelter.totalCapacity}
          </span>
        </div>
        <div className="w-full bg-gray-200 rounded-full h-2">
          <div
            className={`h-2 rounded-full ${getOccupancyColor(occupancyPercentage)}`}
            style={{ width: `${occupancyPercentage}%` }}
          />
        </div>
        <p className="text-xs text-gray-500 mt-1">
          {availableCapacity} spaces available ({occupancyPercentage.toFixed(1)}% full)
        </p>
      </div>

      {shelter.facilities && (
        <div className="mt-3 pt-3 border-t border-gray-200">
          <p className="text-xs text-gray-500">
            <span className="font-medium">🏥 Facilities:</span> {shelter.facilities}
          </p>
        </div>
      )}
    </div>
  );
}
