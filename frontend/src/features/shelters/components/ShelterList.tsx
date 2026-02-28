import React, { useState } from "react";
import { ShelterCard } from "./ShelterCard";
import { ShelterDetail } from "./ShelterDetail";
import { Shelter } from "../types/shelter.types";

interface ShelterListProps {
  shelters: Shelter[];
  onRefresh?: () => void;
}

export function ShelterList({ shelters, onRefresh }: ShelterListProps) {
  const [selectedShelter, setSelectedShelter] = useState<Shelter | null>(null);

  if (shelters.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500 text-lg">No shelters found</p>
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {shelters.map((shelter) => (
          <ShelterCard
            key={shelter.id}
            shelter={shelter}
            onClick={() => setSelectedShelter(shelter)}
          />
        ))}
      </div>

      {selectedShelter && (
        <ShelterDetail
          shelter={selectedShelter}
          onClose={() => setSelectedShelter(null)}
          onUpdate={onRefresh}
        />
      )}
    </>
  );
}
