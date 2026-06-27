import { useEffect, useState } from "react";
import { api } from "./services/api";
import { AppRoutes } from "./routes";

// Interface global para o utilizador
export interface AuthUser {
  id: number;
  nome: string;
  email: string;
  fotoUrl?: string;
  role: string;
  profissionalId?: number;
}

export default function App() {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/usuarios/me")
      .then((res) => {
        setUser(res.data);
      })
      .catch(() => {
        setUser(null);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <div className="h-screen flex items-center justify-center bg-gray-900 text-white font-bold">
        A carregar...
      </div>
    );
  }

  return <AppRoutes user={user} />;
}
