import { useEffect, useState } from "react";
import { api } from "../services/api";
import type { AuthUser } from "../App";

interface Agendamento {
  id: number;
  dataHora: string;
  status: string;
  cliente: { nome: string; email: string };
  servicos: Array<{
    id: number;
    nome: string;
    preco: number;
    duracaoMinutos: number;
  }>;
}

interface ServicoMaster {
  id: number;
  nome: string;
  preco: number;
  duracaoMinutos: number;
}

const DIAS_SEMANA = [
  { key: "MONDAY", label: "Segunda-feira" },
  { key: "TUESDAY", label: "Terça-feira" },
  { key: "WEDNESDAY", label: "Quarta-feira" },
  { key: "THURSDAY", label: "Quinta-feira" },
  { key: "FRIDAY", label: "Sexta-feira" },
  { key: "SATURDAY", label: "Sábado" },
  { key: "SUNDAY", label: "Domingo" },
];

export function BarberDashboard({ user }: { user: AuthUser }) {
  // Controle de Abas: 'agenda' ou 'config'
  const [activeTab, setActiveTab] = useState<"agenda" | "config">("agenda");

  // Estados da Agenda
  const [agendamentos, setAgendamentos] = useState<Agendamento[]>([]);
  const [loadingAgenda, setLoadingAgenda] = useState(true);
  const [error, setError] = useState("");

  // Estados de Configuração (Onboarding)
  const [servicosDisponiveis, setServicosDisponiveis] = useState<
    ServicoMaster[]
  >([]);
  const [servicosSelecionados, setServicosSelecionados] = useState<number[]>(
    [],
  );
  const [escalaTrabalho, setEscalaTrabalho] = useState<
    Record<string, { ativo: boolean; inicio: string; fim: string }>
  >({
    MONDAY: { ativo: true, inicio: "08:00", fim: "18:00" },
    TUESDAY: { ativo: true, inicio: "08:00", fim: "18:00" },
    WEDNESDAY: { ativo: true, inicio: "08:00", fim: "18:00" },
    THURSDAY: { ativo: true, inicio: "08:00", fim: "18:00" },
    FRIDAY: { ativo: true, inicio: "08:00", fim: "18:00" },
    SATURDAY: { ativo: false, inicio: "08:00", fim: "14:00" },
    SUNDAY: { ativo: false, inicio: "08:00", fim: "12:00" },
  });
  const [salvandoConfig, setSalvandoConfig] = useState(false);

  const carregarAgendamentos = () => {
    if (user.profissionalId) {
      setLoadingAgenda(true);
      api
        .get(`/agendamentos/barbeiro/${user.profissionalId}`)
        .then((res) => setAgendamentos(res.data))
        .catch(() => setError("Erro ao carregar a sua agenda."))
        .finally(() => setLoadingAgenda(false));
    }
  };

  // Carrega os dados iniciais
  useEffect(() => {
    carregarAgendamentos();

    // Carrega a lista master de serviços cadastrados na barbearia para o barbeiro escolher
    api
      .get("/servicos")
      .then((res) => setServicosDisponiveis(res.data))
      .catch(() => console.error("Erro ao carregar serviços master"));

    // Se o barbeiro já tiver serviços vinculados, carrega-os para virem marcados
    if (user.profissionalId) {
      api
        .get(`/profissionais-servicos/barbeiro/${user.profissionalId}`)
        .then((res) => {
          const ids = res.data.map((item: any) => item.servico.id);
          setServicosSelecionados(ids);
        });
    }
  }, [user.profissionalId]);

  const handleCancelar = async (id: number) => {
    if (
      window.confirm(
        "Tem a certeza que deseja cancelar o horário deste cliente?",
      )
    ) {
      try {
        await api.patch(`/agendamentos/${id}/cancelar`);
        carregarAgendamentos();
      } catch (err) {
        alert("Erro ao cancelar o agendamento.");
      }
    }
  };

  const handleEsconderVisualmente = (id: number) => {
    setAgendamentos((prev) => prev.filter((a) => a.id !== id));
  };

  // Salva as configurações de Horários e Serviços de uma vez só!
  const handleSalvarConfiguracoes = async () => {
    if (servicosSelecionados.length === 0) {
      alert("Por favor, selecione pelo menos um serviço que você realiza.");
      return;
    }

    for (const [dia, dados] of Object.entries(escalaTrabalho)) {
      if (dados.ativo) {
        // Como o formato é sempre "HH:mm" em 24h no JS, a comparação de strings funciona perfeitamente!
        if (dados.inicio >= dados.fim) {
          const nomeDia = DIAS_SEMANA.find((d) => d.key === dia)?.label;
          alert(
            `Erro na ${nomeDia}: O horário de saída (${dados.fim}) não pode ser antes ou igual ao horário de entrada (${dados.inicio}).`,
          );
          return; // Trava a execução aqui e não envia para o backend!
        }
      }
    }

    setSalvandoConfig(true);
    try {
      // 1. Salva os Serviços Vinculados
      await api.post(
        `/profissionais-servicos/vincular-em-lote/${user.profissionalId}`,
        servicosSelecionados,
      );

      // 2. Formata e Salva a Escala de Horários
      const horariosPayload = Object.entries(escalaTrabalho)
        .filter(([_, dados]) => dados.ativo)
        .map(([dia, dados]) => ({
          diaSemana: dia,
          horaInicio: dados.inicio + ":00",
          horaFim: dados.fim + ":00",
        }));

      await api.post(
        `/horarios/salvar-agenda/${user.profissionalId}`,
        horariosPayload,
      );

      alert(
        "Perfil atualizado com sucesso! Agora você já está visível para os clientes.",
      );
      setActiveTab("agenda");
      carregarAgendamentos();
    } catch (err) {
      alert("Erro ao salvar as configurações de perfil.");
    } finally {
      setSalvandoConfig(false);
    }
  };

  const toggleServicoSelection = (id: number) => {
    setServicosSelecionados((prev) =>
      prev.includes(id) ? prev.filter((sid) => sid !== id) : [...prev, id],
    );
  };

  const handleEscalaChange = (
    dia: string,
    campo: "ativo" | "inicio" | "fim",
    valor: any,
  ) => {
    setEscalaTrabalho((prev) => ({
      ...prev,
      [dia]: { ...prev[dia], [campo]: valor },
    }));
  };

  const formatTime = (isoString: string) =>
    new Date(isoString).toLocaleTimeString("pt-BR", {
      hour: "2-digit",
      minute: "2-digit",
    });
  const formatDate = (isoString: string) =>
    new Date(isoString).toLocaleDateString("pt-BR", {
      day: "2-digit",
      month: "short",
    });
  const handleLogout = async () => {
    try {
      // Tenta derrubar a sessão no backend para invalidar o cookie
      await api.post("/auth/logout").catch(() => api.post("/logout"));
    } catch (err) {
      console.warn("Sessão encerrada.");
    } finally {
      // Só depois de limpar a sessão é que vamos para o login
      window.location.href = "/login";
    }
  };

  return (
    <div className="max-w-md mx-auto bg-[#f9fafb] min-h-screen p-6 font-sans text-gray-800 pb-24">
      {/* Cabeçalho */}
      <div className="flex justify-between items-center mb-6">
        <div>
          <p className="text-gray-500 text-sm font-medium">
            Painel do Profissional
          </p>
          <h1 className="text-2xl font-black text-gray-900">
            Olá, {user.nome}
          </h1>
        </div>
        <button
          onClick={handleLogout}
          className="text-xs font-bold bg-red-50 text-red-600 px-3 py-2 rounded-xl hover:bg-red-100 transition"
        >
          Sair
        </button>
      </div>

      {/* Menu de Abas (Tabs) */}
      <div className="flex bg-gray-200/60 p-1 rounded-2xl mb-6">
        <button
          onClick={() => setActiveTab("agenda")}
          className={`flex-1 py-3 text-sm font-bold rounded-xl transition ${activeTab === "agenda" ? "bg-white text-gray-900 shadow-sm" : "text-gray-500 hover:text-gray-900"}`}
        >
          📅 Minha Agenda
        </button>
        <button
          onClick={() => setActiveTab("config")}
          className={`flex-1 py-3 text-sm font-bold rounded-xl transition ${activeTab === "config" ? "bg-white text-gray-900 shadow-sm" : "text-gray-500 hover:text-gray-900"}`}
        >
          ⚙️ Configurar Perfil
        </button>
      </div>

      {/* ABA 1: LISTA DA AGENDA */}
      {activeTab === "agenda" && (
        <div className="space-y-4">
          {error && (
            <div className="bg-red-100 text-red-700 p-4 rounded-xl text-sm font-bold">
              {error}
            </div>
          )}

          {loadingAgenda ? (
            <p className="text-gray-400 font-medium animate-pulse">
              A carregar agenda...
            </p>
          ) : agendamentos.length === 0 ? (
            <div className="bg-white p-8 rounded-3xl text-center border border-gray-50">
              <span className="text-4xl mb-4 block">☕</span>
              <h3 className="font-bold text-gray-900 mb-1">Agenda Livre</h3>
              <p className="text-sm text-gray-500">
                Nenhum cliente agendado. Aproveite para verificar o seu perfil!
              </p>
            </div>
          ) : (
            agendamentos.map((agendamento) => {
              const totalPreco = agendamento.servicos.reduce(
                (sum, s) => sum + s.preco,
                0,
              );
              const isCancelado = agendamento.status === "CANCELADO";

              return (
                <div
                  key={agendamento.id}
                  className={`p-5 rounded-3xl border flex gap-4 ${isCancelado ? "bg-gray-50 border-gray-200 opacity-70" : "bg-white border-gray-50 shadow-sm"}`}
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
                      <h3
                        className={`font-bold truncate ${isCancelado ? "text-gray-500 line-through" : "text-gray-900"}`}
                      >
                        {agendamento.cliente.nome}
                      </h3>
                      <div className="flex items-center gap-1">
                        <span
                          className={`text-[10px] font-bold px-2 py-0.5 rounded-lg ${isCancelado ? "bg-red-100 text-red-700" : "bg-green-100 text-green-700"}`}
                        >
                          {agendamento.status}
                        </span>
                        {isCancelado && (
                          <button
                            onClick={() =>
                              handleEsconderVisualmente(agendamento.id)
                            }
                            className="text-gray-400 hover:text-gray-700 font-bold px-1 text-xs"
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
            })
          )}
        </div>
      )}

      {/* ABA 2: CONFIGURAÇÃO DE SERVIÇOS E HORÁRIOS (ONBOARDING) */}
      {activeTab === "config" && (
        <div className="space-y-6">
          {/* Seção de Serviços */}
          <div className="bg-white p-5 rounded-3xl shadow-sm border border-gray-100">
            <h2 className="text-lg font-black text-gray-900 mb-1">
              Meus Serviços
            </h2>
            <p className="text-gray-400 text-xs font-medium mb-4">
              Selecione os cortes e tratamentos que você sabe realizar:
            </p>

            <div className="space-y-2">
              {servicosDisponiveis.map((s) => {
                const isSelected = servicosSelecionados.includes(s.id);
                return (
                  <div
                    key={s.id}
                    onClick={() => toggleServicoSelection(s.id)}
                    className={`p-3 rounded-xl border-2 cursor-pointer transition flex justify-between items-center ${isSelected ? "border-gray-900 bg-gray-50/50" : "border-gray-100 hover:border-gray-200"}`}
                  >
                    <div>
                      <p className="font-bold text-sm text-gray-900">
                        {s.nome}
                      </p>
                      <p className="text-xs text-gray-400">
                        {s.duracaoMinutos} min
                      </p>
                    </div>
                    <p className="font-black text-sm text-gray-900">
                      R$ {s.preco.toFixed(2)}
                    </p>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Seção de Horários */}
          <div className="bg-white p-5 rounded-3xl shadow-sm border border-gray-100">
            <h2 className="text-lg font-black text-gray-900 mb-1">
              Horário de Trabalho
            </h2>
            <p className="text-gray-400 text-xs font-medium mb-4">
              Ative os seus dias de atendimento e defina o horário de
              expediente:
            </p>

            <div className="space-y-3">
              {DIAS_SEMANA.map((dia) => {
                const config = escalaTrabalho[dia.key];
                return (
                  <div
                    key={dia.key}
                    className="flex items-center justify-between p-2 rounded-xl hover:bg-gray-50/50 transition"
                  >
                    {/* Checkbox Liga/Desliga o Dia */}
                    <label className="flex items-center gap-2 cursor-pointer min-w-[120px]">
                      <input
                        type="checkbox"
                        checked={config.ativo}
                        onChange={(e) =>
                          handleEscalaChange(dia.key, "ativo", e.target.checked)
                        }
                        className="rounded border-gray-300 text-gray-900 focus:ring-gray-900 w-4 h-4"
                      />
                      <span
                        className={`text-sm font-bold ${config.ativo ? "text-gray-900" : "text-gray-400"}`}
                      >
                        {dia.label}
                      </span>
                    </label>

                    {/* Inputs de Hora Entrada / Saída */}
                    {config.ativo ? (
                      <div className="flex items-center gap-1.5">
                        <input
                          type="time"
                          value={config.inicio}
                          onChange={(e) =>
                            handleEscalaChange(
                              dia.key,
                              "inicio",
                              e.target.value,
                            )
                          }
                          className="bg-gray-100 border-none p-1.5 rounded-lg text-xs font-bold text-gray-700 outline-none w-18 text-center"
                        />
                        <span className="text-gray-400 text-xs font-bold">
                          às
                        </span>
                        <input
                          type="time"
                          value={config.fim}
                          onChange={(e) =>
                            handleEscalaChange(dia.key, "fim", e.target.value)
                          }
                          className="bg-gray-100 border-none p-1.5 rounded-lg text-xs font-bold text-gray-700 outline-none w-18 text-center"
                        />
                      </div>
                    ) : (
                      <span className="text-xs font-bold text-red-400 bg-red-50 px-2 py-1 rounded-md">
                        Folga
                      </span>
                    )}
                  </div>
                );
              })}
            </div>
          </div>

          {/* Botão de Gravar Configurações */}
          <button
            onClick={handleSalvarConfiguracoes}
            disabled={salvandoConfig}
            className="w-full bg-gray-900 text-white font-black py-4 rounded-2xl hover:bg-gray-800 transition shadow-lg disabled:bg-gray-300"
          >
            {salvandoConfig
              ? "A Gravar Alterações..."
              : "Salvar Configurações do Perfil"}
          </button>
        </div>
      )}
    </div>
  );
}
