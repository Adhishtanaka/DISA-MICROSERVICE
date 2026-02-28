import { BrowserRouter, Routes, Route, Navigate } from "react-router";
import { MissionsPage } from "../features/missions";
import { IncidentsPage } from "../features/incidents";
import { AssessmentsPage } from "../features/assessments";
import { SheltersPage } from "../features/shelters";
import { TasksPage } from "../features/tasks";
import { LoginPage, RegisterPage, useAuthStore } from "../features/auth";
import { Navigation } from "../components/Navigation";

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  return <>{children}</>;
}

function AppLayout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <Navigation />
      {children}
    </>
  );
}

export function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public routes */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Protected routes */}
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <Navigate to="/missions" replace />
            </ProtectedRoute>
          }
        />
        <Route
          path="/missions"
          element={
            <ProtectedRoute>
              <AppLayout><MissionsPage /></AppLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/incidents"
          element={
            <ProtectedRoute>
              <AppLayout><IncidentsPage /></AppLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/assessments"
          element={
            <ProtectedRoute>
              <AppLayout><AssessmentsPage /></AppLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/shelters"
          element={
            <ProtectedRoute>
              <AppLayout><SheltersPage /></AppLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/tasks"
          element={
            <ProtectedRoute>
              <AppLayout><TasksPage /></AppLayout>
            </ProtectedRoute>
          }
        />

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
