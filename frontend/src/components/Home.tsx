import { useEffect, useState } from "react";
import { api } from "../services/api";
import { useNavigate } from "react-router-dom";
import type { AuthUser } from "../App";

interface Barbeiro {
  id: number;
  nome: string;
  fotoUrl: string;
  notaMedia: number;
  totalAvaliacoes: number;
}

/**
 * Tela inicial do cliente (Home).
 * Responsável por listar os profissionais disponíveis e fornecer atalhos para a agenda e logout.
 */
export function Home({ user }: { user: AuthUser }) {
  const [barbers, setBarbers] = useState<Barbeiro[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    api
      .get("/profissionais")
      .then((res) => setBarbers(res.data))
      .catch((err) => console.error("Erro ao carregar os barbeiros", err));
  }, []);

  const getInitialsAvatar = (name: string) => {
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=111827&color=ffffff&bold=true`;
  };

  /**
   * Tenta invalidar a sessão ativa no backend (Spring Security)
   * e redireciona para o login, limpando o estado global do frontend.
   */
  const handleLogout = async () => {
    try {
      await api.post("/auth/logout").catch(() => api.post("/logout"));
    } catch (err) {
      console.warn("Sessão encerrada no cliente.");
    } finally {
      window.location.href = "/login";
    }
  };

  return (
    <div className="max-w-md mx-auto bg-[#f9fafb] min-h-screen p-6 font-sans text-gray-800 pb-24">
      {/* HEADER: Saudação e Menu do Usuário */}
      <div className="flex justify-between items-center mb-8">
        <div>
          <p className="text-gray-500 text-sm font-medium">Olá,</p>
          <h1 className="text-2xl font-bold text-gray-900">{user.nome}</h1>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={() => navigate("/meus-agendamentos")}
            className="text-xs font-bold bg-gray-100 text-gray-700 px-3 py-2 rounded-xl hover:bg-gray-200 transition"
          >
            Agenda
          </button>

          <button
            onClick={handleLogout}
            className="text-xs font-bold bg-red-50 text-red-600 px-3 py-2 rounded-xl hover:bg-red-100 transition"
          >
            Sair
          </button>

          <div className="w-12 h-12 rounded-full overflow-hidden shadow-sm border-2 border-white bg-gray-200 ml-1">
            <img
              src={user.fotoUrl || getInitialsAvatar(user.nome)}
              alt="User Profile"
              className="w-full h-full object-cover"
              referrerPolicy="no-referrer"
            />
          </div>
        </div>
      </div>

      {/* BARRA DE BUSCA (Apenas visual por enquanto) */}
      <div className="relative mb-8 shadow-sm rounded-2xl">
        <input
          type="text"
          placeholder="O que procura?"
          className="w-full bg-white rounded-2xl py-4 pl-12 pr-4 focus:ring-2 focus:ring-gray-200 outline-none text-sm border border-gray-100"
        />
        <span className="absolute left-4 top-4 text-gray-400 opacity-70">
          🔍
        </span>
      </div>

      <div className="flex justify-between items-center mb-5">
        <h2 className="text-lg font-bold text-gray-900">
          Profissionais disponíveis
        </h2>
      </div>

      {/* LISTAGEM DE BARBEIROS */}
      <div className="space-y-4">
        {barbers.length === 0 ? (
          <p className="text-gray-500 text-center py-4">
            Nenhum barbeiro disponível no momento.
          </p>
        ) : (
          barbers.map((barber) => (
            <div
              key={barber.id}
              className="bg-white rounded-2xl p-4 flex items-center justify-between shadow-sm border border-gray-50 hover:shadow-md transition-shadow"
            >
              <div className="flex items-center gap-4">
                <div className="relative">
                  <img
                    src={barber.fotoUrl || getInitialsAvatar(barber.nome)}
                    alt={barber.nome}
                    className="w-14 h-14 rounded-full object-cover border-2 border-gray-50"
                  />
                  <div className="absolute bottom-0 right-0 w-3.5 h-3.5 bg-green-400 border-2 border-white rounded-full"></div>
                </div>
                <div>
                  <h3 className="font-bold text-gray-900">{barber.nome}</h3>
                  <div className="flex items-center text-xs text-gray-400 mt-1 font-medium">
                    <span className="text-yellow-400 mr-1 text-sm">⭐</span>
                    <span>
                      {barber.notaMedia > 0
                        ? barber.notaMedia.toFixed(1)
                        : "Novo"}{" "}
                      ({barber.totalAvaliacoes} reviews)
                    </span>
                  </div>
                </div>
              </div>

              <button
                onClick={() => navigate(`/book/${barber.id}`)}
                className="bg-gray-900 text-white text-xs font-bold px-4 py-2.5 rounded-xl hover:bg-gray-700 transition-colors shadow-sm"
              >
                Agendar
              </button>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
