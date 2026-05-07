import { Routes, Route, Navigate } from "react-router-dom";
import { Login } from "./components/Login";
import { Register } from "./components/Register";
import { Home } from "./components/Home";
import type { JSX } from "react/jsx-runtime";

// Criamos um mini-componente para proteger as rotas que exigem login
interface PrivateRouteProps {
  isAuthenticated: boolean;
  children: JSX.Element;
}

const PrivateRoute = ({ isAuthenticated, children }: PrivateRouteProps) => {
  return isAuthenticated ? children : <Navigate to="/login" />;
};

// Este é o componente principal de Rotas que será exportado
interface AppRoutesProps {
  isAuthenticated: boolean;
}

export function AppRoutes({ isAuthenticated }: AppRoutesProps) {
  return (
    <Routes>
      {/* Rotas Públicas (Qualquer um pode acessar) */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Rotas Privadas (Exigem Login) */}
      <Route
        path="/"
        element={
          <PrivateRoute isAuthenticated={isAuthenticated}>
            <Home />
          </PrivateRoute>
        }
      />
    </Routes>
  );
}
