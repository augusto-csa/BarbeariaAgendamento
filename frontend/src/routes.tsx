import { Routes, Route, Navigate } from "react-router-dom";
import { Login } from "./components/Login";
import { Register } from "./components/Register";
import { Home } from "./components/Home";
import { Booking } from "./components/Booking";
import { BarberDashboard } from "./components/BarberDashboard";
import type { AuthUser } from "./App";
import type { JSX } from "react/jsx-runtime";
import { ClientAppointments } from "./components/ClientAppointments";

interface PrivateRouteProps {
  user: AuthUser | null;
  allowedRoles?: string[];
  children: JSX.Element;
}

/**
 * Componente de proteção de rotas (Route Guard).
 * Intercepta o acesso garantindo que o usuário esteja logado e possua o nível de acesso (Role) exigido.
 * Caso contrário, redireciona para a página de login ou para a rota adequada ao seu perfil.
 */
const PrivateRoute = ({ user, allowedRoles, children }: PrivateRouteProps) => {
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return user.role === "BARBEIRO" ? (
      <Navigate to="/painel-barbeiro" replace />
    ) : (
      <Navigate to="/" replace />
    );
  }

  return children;
};

interface AppRoutesProps {
  user: AuthUser | null;
}

/**
 * Configuração central do roteamento da aplicação.
 * Implementa a lógica de triagem inicial: se um usuário autenticado tentar acessar a tela de login/registro,
 * ele é automaticamente redirecionado para o seu respectivo painel (Home para Clientes, Dashboard para Barbeiros).
 */
export function AppRoutes({ user }: AppRoutesProps) {
  return (
    <Routes>
      <Route
        path="/login"
        element={
          user ? (
            <Navigate
              to={user.role === "BARBEIRO" ? "/painel-barbeiro" : "/"}
              replace
            />
          ) : (
            <Login />
          )
        }
      />
      <Route
        path="/register"
        element={
          user ? (
            <Navigate
              to={user.role === "BARBEIRO" ? "/painel-barbeiro" : "/"}
              replace
            />
          ) : (
            <Register />
          )
        }
      />

      <Route
        path="/"
        element={
          <PrivateRoute user={user} allowedRoles={["CLIENTE", "ADMIN"]}>
            <Home user={user} />
          </PrivateRoute>
        }
      />

      <Route
        path="/book/:barberId"
        element={
          <PrivateRoute user={user} allowedRoles={["CLIENTE", "ADMIN"]}>
            <Booking user={user} />
          </PrivateRoute>
        }
      />

      <Route
        path="/painel-barbeiro"
        element={
          <PrivateRoute user={user} allowedRoles={["BARBEIRO", "ADMIN"]}>
            <BarberDashboard user={user} />
          </PrivateRoute>
        }
      />

      <Route
        path="/meus-agendamentos"
        element={
          <PrivateRoute user={user} allowedRoles={["CLIENTE", "ADMIN"]}>
            <ClientAppointments user={user} />
          </PrivateRoute>
        }
      />
    </Routes>
  );
}
