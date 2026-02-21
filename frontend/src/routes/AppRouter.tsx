import { BrowserRouter, Routes, Route, Navigate, NavLink } from 'react-router';
import { Activity, Database } from 'lucide-react';
import { MissionsPage } from '../features/missions';
import { ResourcesPage } from '../features/resources';

export function AppRouter() {
  return (
    <BrowserRouter>
      <AppNav />
      <Routes>
        <Route path="/" element={<Navigate to="/missions" replace />} />
        <Route path="/missions" element={<MissionsPage />} />
        <Route path="/resources" element={<ResourcesPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}

function AppNav() {
  const base =
    'flex items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium transition-colors';
  const active = 'bg-blue-50 text-blue-700';
  const inactive = 'text-gray-600 hover:bg-gray-100 hover:text-gray-900';

  return (
    <nav className="sticky top-0 z-50 flex items-center gap-1 border-b border-gray-200 bg-white px-4 py-2 shadow-sm">
      <NavLink
        to="/missions"
        className={({ isActive }) => `${base} ${isActive ? active : inactive}`}
      >
        <Activity className="h-4 w-4" />
        Missions
      </NavLink>
      <NavLink
        to="/resources"
        className={({ isActive }) => `${base} ${isActive ? active : inactive}`}
      >
        <Database className="h-4 w-4" />
        Resources
      </NavLink>
    </nav>
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
