import { useEffect, useState } from "react";
import { api } from "./services/api";
import { AppRoutes } from "./routes"; // Importamos o arquivo que acabamos de criar

export default function App() {
  const [auth, setAuth] = useState<"loading" | "yes" | "no">("loading");

  useEffect(() => {
    // Verifica se o cookie de sessão é válido lá no Backend
    api
      .get("/usuarios/me")
      .then(() => setAuth("yes"))
      .catch(() => setAuth("no"));
  }, []);

  // Tela de Loading Global (aparece rapidamente enquanto o React fala com o Java)
  if (auth === "loading") {
    return (
      <div className="h-screen flex items-center justify-center bg-gray-900 text-white font-bold">
        Loading...
      </div>
    );
  }

  // Passa o resultado da verificação para o nosso novo arquivo de rotas
  return <AppRoutes isAuthenticated={auth === "yes"} />;
}
