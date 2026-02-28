import { BrowserRouter, Routes, Route, Navigate } from "react-router";
import { MissionsPage } from "../features/missions";
import { IncidentsPage } from "../features/incidents";
import { AssessmentsPage } from "../features/assessments";
import { SheltersPage } from "../features/shelters";
import { Navigation } from "../components/Navigation";

export function AppRouter() {
  return (
    <BrowserRouter>
      <Navigation />
      <Routes>
        <Route path="/" element={<Navigate to="/missions" replace />} />
        <Route path="/missions" element={<MissionsPage />} />
        <Route path="/incidents" element={<IncidentsPage />} />
        <Route path="/assessments" element={<AssessmentsPage />} />
        <Route path="/shelters" element={<SheltersPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}

function NotFoundPage() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-4 bg-gray-50 text-center">
      <p className="text-6xl font-black text-gray-200">404</p>
      <h1 className="text-xl font-semibold text-gray-700">Page not found</h1>
      <a href="/missions" className="text-sm text-blue-600 hover:underline">
        Go to Missions
      </a>
    </div>
  );
}