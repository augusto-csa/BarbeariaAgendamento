import { useEffect, useState } from "react";
import { api } from "../services/api";
import type { AuthUser } from "../App";

interface Agendamento {
  id: number;
  dataHora: string;
  status: string;
  cliente: {
    nome: string;
    email: string;
  };
  servicos: Array<{
    id: number;
    nome: string;
    preco: number;
    duracaoMinutos: number;
  }>;
}

export function BarberDashboard({ user }: { user: AuthUser }) {
  const [agendamentos, setAgendamentos] = useState<Agendamento[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (user.profissionalId) {
      api
        .get(`/agendamentos/barbeiro/${user.profissionalId}`)
        .then((res) => setAgendamentos(res.data))
        .catch(() => setError("Erro ao carregar a sua agenda."))
        .finally(() => setLoading(false));
    } else {
      setError("Perfil de profissional não encontrado.");
      setLoading(false);
    }
  }, [user.profissionalId]);

  const formatTime = (isoString: string) => {
    return new Date(isoString).toLocaleTimeString("pt-BR", {
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const formatDate = (isoString: string) => {
    return new Date(isoString).toLocaleDateString("pt-BR", {
      day: "2-digit",
      month: "short",
    });
  };

  // Logout simples: recarrega a página ou redireciona
  const handleLogout = () => {
    window.location.href = "/login";
  };

  return (
    <div className="max-w-md mx-auto bg-[#f9fafb] min-h-screen p-6 font-sans text-gray-800 pb-24">
      <div className="flex justify-between items-center mb-8">
        <div>
          <p className="text-gray-500 text-sm font-medium">
            Painel do Profissional
          </p>
          <h1 className="text-2xl font-black text-gray-900">
            Agenda de {user.nome}
          </h1>
        </div>
        <button
          onClick={handleLogout}
          className="text-sm font-bold text-red-500 hover:underline"
        >
          Sair
        </button>
      </div>

      {error && (
        <div className="bg-red-100 text-red-700 p-4 rounded-xl mb-6 text-sm font-bold border border-red-200">
          {error}
        </div>
      )}

      {loading ? (
        <p className="text-gray-400 font-medium animate-pulse">
          A carregar agenda...
        </p>
      ) : agendamentos.length === 0 ? (
        <div className="bg-white p-8 rounded-3xl text-center shadow-sm border border-gray-50">
          <span className="text-4xl mb-4 block">☕</span>
          <h3 className="font-bold text-gray-900 mb-2">Agenda Livre</h3>
          <p className="text-sm text-gray-500">Ainda não tens agendamentos.</p>
        </div>
      ) : (
        <div className="space-y-4">
          {agendamentos.map((agendamento) => {
            const totalPreco = agendamento.servicos.reduce(
              (sum, s) => sum + s.preco,
              0,
            );

            return (
              <div
                key={agendamento.id}
                className="bg-white p-5 rounded-3xl shadow-sm border border-gray-50 flex gap-4"
              >
                <div className="flex flex-col items-center justify-center border-r border-gray-100 pr-4 min-w-[70px]">
                  <span className="text-lg font-black text-gray-900">
                    {formatTime(agendamento.dataHora)}
                  </span>
                  <span className="text-xs font-bold text-gray-400 uppercase tracking-wider">
                    {formatDate(agendamento.dataHora)}
                  </span>
                </div>

                <div className="flex-1">
                  <div className="flex justify-between items-start mb-1">
                    <h3 className="font-bold text-gray-900 truncate">
                      {agendamento.cliente.nome}
                    </h3>
                    <span className="text-xs font-bold px-2 py-1 bg-green-100 text-green-700 rounded-lg">
                      {agendamento.status}
                    </span>
                  </div>

                  <div className="text-sm text-gray-500 font-medium mb-3">
                    {agendamento.servicos.map((s) => s.nome).join(" + ")}
                  </div>

                  <div className="flex justify-between items-center mt-2 pt-3 border-t border-gray-50">
                    <span className="text-xs text-gray-400 font-bold">
                      Total a receber:
                    </span>
                    <span className="font-black text-gray-900">
                      R$ {totalPreco.toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
