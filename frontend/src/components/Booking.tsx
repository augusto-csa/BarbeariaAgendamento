import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { api } from "../services/api";
import type { AuthUser } from "../App";

interface ServicoVinculado {
  id: number;
  servico: {
    id: number;
    nome: string;
    preco: number;
    duracaoMinutos: number;
  };
}

const dayMap: Record<string, number> = {
  SUNDAY: 0,
  MONDAY: 1,
  TUESDAY: 2,
  WEDNESDAY: 3,
  THURSDAY: 4,
  FRIDAY: 5,
  SATURDAY: 6,
};

const nomeDias: Record<number, string> = {
  0: "Domingo",
  1: "Segunda",
  2: "Terça",
  3: "Quarta",
  4: "Quinta",
  5: "Sexta",
  6: "Sábado",
};

/**
 * Componente de fluxo de agendamento (Booking) para o cliente.
 * Gerencia a seleção de serviços, validação de dias de trabalho do profissional,
 * e consulta dinâmica de horários livres no backend.
 */
export function Booking({ user }: { user: AuthUser }) {
  const { barberId } = useParams();
  const navigate = useNavigate();

  const [servicos, setServicos] = useState<ServicoVinculado[]>([]);
  const [selectedServices, setSelectedServices] = useState<number[]>([]);

  const [data, setData] = useState("");
  const [hora, setHora] = useState("");

  const [horariosLivres, setHorariosLivres] = useState<string[]>([]);
  const [loadingHorarios, setLoadingHorarios] = useState(false);
  const [diasTrabalho, setDiasTrabalho] = useState<number[]>([]);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  /**
   * Carrega os serviços que o profissional realiza e a sua escala de trabalho (dias da semana)
   * assim que o componente é montado.
   */
  useEffect(() => {
    api
      .get(`/profissionais-servicos/barbeiro/${barberId}`)
      .then((res) => setServicos(res.data))
      .catch(() => setError("Erro ao carregar serviços."));

    api
      .get(`/horarios/barbeiro/${barberId}/dias`)
      .then((res) => {
        const diasEmNumeros = res.data.map((d: string) => dayMap[d]);
        setDiasTrabalho(diasEmNumeros);
      })
      .catch(() =>
        console.warn("Não foi possível carregar os dias de trabalho."),
      );
  }, [barberId]);

  /**
   * Dispara a consulta de horários disponíveis no backend sempre que uma data válida é selecionada.
   * O backend calcula as vagas considerando a duração dos serviços e os agendamentos já existentes.
   */
  useEffect(() => {
    if (data && barberId) {
      setLoadingHorarios(true);
      setHora("");

      api
        .get(
          `/agendamentos/disponibilidade?profissionalId=${barberId}&data=${data}`,
        )
        .then((res) => setHorariosLivres(res.data))
        .catch(() => setError("Erro ao buscar horários livres."))
        .finally(() => setLoadingHorarios(false));
    }
  }, [data, barberId]);

  const toggleService = (id: number) => {
    setSelectedServices((prev) =>
      prev.includes(id) ? prev.filter((s) => s !== id) : [...prev, id],
    );
  };

  /**
   * Intercepta a mudança de data para realizar validação no lado do cliente (Client-side validation).
   * Impede que o usuário selecione um dia da semana em que o barbeiro não possui escala configurada.
   */
  const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selected = e.target.value;

    if (!selected) {
      setData("");
      setHorariosLivres([]);
      return;
    }

    const dayOfWeek = new Date(selected + "T00:00:00").getDay();

    if (diasTrabalho.length > 0 && !diasTrabalho.includes(dayOfWeek)) {
      setError(
        "Este profissional não atende neste dia da semana. Escolha outra data.",
      );
      setData("");
      setHora("");
      setHorariosLivres([]);
      return;
    }

    setError("");
    setData(selected);
  };

  /**
   * Consolida os dados e envia o pedido de agendamento ao backend.
   */
  const handleBooking = async () => {
    if (selectedServices.length === 0 || !data || !hora) {
      setError("Selecione os serviços, a data e a hora.");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const dataHoraIso = `${data}T${hora}:00`;

      const payload = {
        clienteId: user.id,
        profissionalId: Number(barberId),
        dataHora: dataHoraIso,
        servicoIds: selectedServices,
      };

      await api.post("/agendamentos", payload);
      alert("Agendamento confirmado com sucesso!");
      navigate("/");
    } catch (err: any) {
      setError(err.response?.data?.message || "O horário ficou indisponível.");
    } finally {
      setLoading(false);
    }
  };

  const getTodayDateString = () => new Date().toISOString().split("T")[0];

  return (
    <div className="max-w-md mx-auto bg-[#f9fafb] min-h-screen p-6 font-sans text-gray-800 pb-24">
      <button
        onClick={() => navigate(-1)}
        className="mb-6 text-xl font-bold flex items-center gap-2 hover:text-gray-500 transition"
      >
        <span>←</span> Voltar
      </button>

      <h1 className="text-2xl font-black text-gray-900 mb-6">
        Escolha os Serviços
      </h1>

      {error && (
        <div className="bg-red-100 text-red-700 p-4 rounded-xl mb-6 text-sm font-bold border border-red-200">
          {error}
        </div>
      )}

      <div className="space-y-3 mb-8">
        {servicos.length === 0 && !error ? (
          <p className="text-gray-500 text-sm">
            Este profissional ainda não tem serviços registados.
          </p>
        ) : (
          servicos.map((item) => {
            const s = item.servico;
            const isSelected = selectedServices.includes(s.id);
            return (
              <div
                key={item.id}
                onClick={() => toggleService(s.id)}
                className={`p-4 rounded-2xl border-2 cursor-pointer transition-all ${
                  isSelected
                    ? "border-gray-900 bg-gray-50 shadow-md"
                    : "border-transparent bg-white shadow-sm hover:border-gray-200"
                }`}
              >
                <div className="flex justify-between items-center">
                  <div>
                    <h3 className="font-bold text-gray-900">{s.nome}</h3>
                    <p className="text-sm text-gray-400 font-medium">
                      {s.duracaoMinutos} min
                    </p>
                  </div>
                  <div className="text-lg font-black text-gray-900">
                    R$ {s.preco.toFixed(2)}
                  </div>
                </div>
              </div>
            );
          })
        )}
      </div>

      <h2 className="text-xl font-black text-gray-900 mb-4">Data & Hora</h2>

      {diasTrabalho.length > 0 && (
        <div className="mb-4 p-3 bg-blue-50 text-blue-700 rounded-xl text-xs font-bold border border-blue-100 flex items-center gap-2">
          <span>ℹ️</span>
          <span>
            Atende:{" "}
            {diasTrabalho
              .sort()
              .map((d) => nomeDias[d])
              .join(", ")}
          </span>
        </div>
      )}

      <div className="mb-6">
        <input
          type="date"
          value={data}
          min={getTodayDateString()}
          onChange={handleDateChange}
          className="w-full p-4 rounded-2xl bg-white shadow-sm border border-gray-100 outline-none focus:ring-2 focus:ring-gray-900 font-bold text-gray-700"
        />
      </div>

      {data && (
        <div className="mb-8">
          <h3 className="text-sm font-bold text-gray-500 mb-3 uppercase tracking-wider">
            Horários Livres
          </h3>

          {loadingHorarios ? (
            <p className="text-gray-400 text-sm font-medium animate-pulse">
              A procurar vagas...
            </p>
          ) : horariosLivres.length === 0 ? (
            <div className="bg-orange-50 text-orange-600 p-4 rounded-xl text-sm font-bold border border-orange-100">
              Não há vagas disponíveis neste dia.
            </div>
          ) : (
            <div className="grid grid-cols-3 gap-3">
              {horariosLivres.map((h) => (
                <button
                  key={h}
                  onClick={() => setHora(h)}
                  className={`py-3 rounded-xl font-bold transition-all border-2 ${
                    hora === h
                      ? "bg-gray-900 text-white border-gray-900 shadow-md transform scale-105"
                      : "bg-white text-gray-700 border-transparent hover:border-gray-300 shadow-sm"
                  }`}
                >
                  {h}
                </button>
              ))}
            </div>
          )}
        </div>
      )}

      <button
        onClick={handleBooking}
        disabled={loading || selectedServices.length === 0 || !data || !hora}
        className={`w-full font-black py-4 rounded-2xl transition shadow-lg mt-4 ${
          loading || selectedServices.length === 0 || !data || !hora
            ? "bg-gray-300 text-gray-500 cursor-not-allowed"
            : "bg-gray-900 text-white hover:bg-gray-800"
        }`}
      >
        {loading ? "A Processar..." : "Confirmar Agendamento"}
      </button>
    </div>
  );
}
