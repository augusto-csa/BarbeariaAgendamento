import { useEffect, useState } from "react";
import { api } from "../services/api";
import type { AuthUser } from "../App";
import { useNavigate } from "react-router-dom";

interface Agendamento {
  id: number;
  dataHora: string;
  status: string;
  profissional: {
    usuario: {
      nome: string;
    };
  };
  servicos: Array<{
    id: number;
    nome: string;
    preco: number;
    duracaoMinutos: number;
  }>;
}

export function ClientAppointments({ user }: { user: AuthUser }) {
  const [agendamentos, setAgendamentos] = useState<Agendamento[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const carregarAgendamentos = () => {
    setLoading(true);
    api
      .get(`/agendamentos/cliente/${user.id}`)
      .then((res) => setAgendamentos(res.data))
      .catch(() => setError("Erro ao carregar os seus agendamentos."))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    carregarAgendamentos();
  }, [user.id]);

  // NOVA FUNÇÃO: Cancelar Agendamento
  const handleCancelar = async (id: number) => {
    if (window.confirm("Tem a certeza que deseja cancelar este agendamento?")) {
      try {
        await api.patch(`/agendamentos/${id}/cancelar`);
        carregarAgendamentos(); // Recarrega a lista para atualizar o status na tela
      } catch (err) {
        alert("Erro ao cancelar o agendamento.");
      }
    }
  };

  const handleEsconderVisualmente = (id: number) => {
    setAgendamentos((prev) =>
      prev.filter((agendamento) => agendamento.id !== id),
    );
  };

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

  return (
    <div className="max-w-md mx-auto bg-[#f9fafb] min-h-screen p-6 font-sans text-gray-800 pb-24">
      {/* Cabeçalho */}
      <div className="flex items-center gap-4 mb-8">
        <button
          onClick={() => navigate(-1)}
          className="text-xl font-bold hover:text-gray-500 transition"
        >
          ←
        </button>
        <div>
          <h1 className="text-2xl font-black text-gray-900">
            Meus Agendamentos
          </h1>
          <p className="text-gray-500 text-sm font-medium">
            Os seus próximos cortes
          </p>
        </div>
      </div>

      {/* Alertas */}
      {error && (
        <div className="bg-red-100 text-red-700 p-4 rounded-xl mb-6 text-sm font-bold border border-red-200">
          {error}
        </div>
      )}

      {/* Lista de Agendamentos */}
      {loading ? (
        <p className="text-gray-400 font-medium animate-pulse">
          A procurar os seus agendamentos...
        </p>
      ) : agendamentos.length === 0 ? (
        <div className="bg-white p-8 rounded-3xl text-center shadow-sm border border-gray-50 mt-4">
          <span className="text-4xl mb-4 block">📅</span>
          <h3 className="font-bold text-gray-900 mb-2">Sem marcações</h3>
          <p className="text-sm text-gray-500">
            Ainda não marcou nenhum serviço. Que tal dar um trato no visual
            hoje?
          </p>
          <button
            onClick={() => navigate("/")}
            className="mt-6 bg-gray-900 text-white text-sm font-bold px-6 py-3 rounded-xl hover:bg-gray-800 transition-colors shadow-md"
          >
            Encontrar Barbeiro
          </button>
        </div>
      ) : (
        <div className="space-y-4">
          {agendamentos.map((agendamento) => {
            const totalPreco = agendamento.servicos.reduce(
              (sum, s) => sum + s.preco,
              0,
            );
            const isCancelado = agendamento.status === "CANCELADO";

            return (
              <div
                key={agendamento.id}
                className={`p-5 rounded-3xl shadow-sm border flex gap-4 ${isCancelado ? "bg-gray-50 border-gray-200 opacity-70" : "bg-white border-gray-50"}`}
              >
                {/* Data e Hora */}
                <div className="flex flex-col items-center justify-center border-r border-gray-100 pr-4 min-w-[70px]">
                  <span className="text-lg font-black text-gray-900">
                    {formatTime(agendamento.dataHora)}
                  </span>
                  <span className="text-xs font-bold text-gray-400 uppercase tracking-wider">
                    {formatDate(agendamento.dataHora)}
                  </span>
                </div>

                {/* Detalhes do Serviço */}
                <div className="flex-1">
                  <div className="flex justify-between items-start mb-1">
                    <h3
                      className={`font-bold truncate ${isCancelado ? "text-gray-500 line-through" : "text-gray-900"}`}
                    >
                      Com {agendamento.profissional.usuario.nome}
                    </h3>
                    <div className="flex items-center gap-2">
                      <span
                        className={`text-[10px] font-bold px-2 py-1 rounded-lg ${isCancelado ? "bg-red-100 text-red-700" : "bg-green-100 text-green-700"}`}
                      >
                        {agendamento.status}
                      </span>
                      {/* Botão de fechar se estiver cancelado */}
                      {isCancelado && (
                        <button
                          onClick={() =>
                            handleEsconderVisualmente(agendamento.id)
                          }
                          className="text-gray-400 hover:text-gray-700 font-bold px-1 transition"
                          title="Esconder da lista"
                        >
                          ✕
                        </button>
                      )}
                    </div>
                  </div>

                  <div className="text-sm text-gray-500 font-medium mb-3">
                    {agendamento.servicos.map((s) => s.nome).join(" + ")}
                  </div>

                  <div className="flex justify-between items-center mt-2 pt-3 border-t border-gray-50">
                    <span className="font-black text-gray-900">
                      R$ {totalPreco.toFixed(2)}
                    </span>

                    {/* Botão de Cancelar - Só aparece se não estiver cancelado */}
                    {!isCancelado && (
                      <button
                        onClick={() => handleCancelar(agendamento.id)}
                        className="text-xs font-bold text-red-500 bg-red-50 px-3 py-1.5 rounded-lg hover:bg-red-100 transition"
                      >
                        Cancelar
                      </button>
                    )}
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
