import { useEffect, useState } from "react";
import { api } from "../services/api";

// 1. Criamos a interface que reflete perfeitamente o nosso ProfissionalResponseDTO do Java
interface Barbeiro {
  id: number;
  nome: string;
  fotoUrl: string;
  notaMedia: number;
  totalAvaliacoes: number;
}

export function Home() {
  const [user, setUser] = useState<any>(null);
  const [barbers, setBarbers] = useState<Barbeiro[]>([]); // 2. Novo estado para os barbeiros
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Busca o usuário logado
    api
      .get("/usuarios/me")
      .then((res) => {
        setUser(res.data);
        setLoading(false);
      })
      .catch(() => {
        setUser(null);
        setLoading(false);
      });

    // 3. Busca a lista de profissionais reais do nosso Backend
    api
      .get("/profissionais")
      .then((res) => {
        setBarbers(res.data);
      })
      .catch((err) => console.error("Erro ao carregar os barbeiros", err));
  }, []);

  return (
    <div className="max-w-md mx-auto bg-[#f9fafb] min-h-screen p-6 font-sans text-gray-800">
      {/* Header do Usuário */}
      <div className="flex justify-between items-center mb-8">
        <div>
          <p className="text-gray-500 text-sm font-medium">Hello!</p>
          <h1 className="text-2xl font-bold text-gray-900">
            {loading ? "..." : user?.name || "Guest"}
          </h1>
        </div>
        <div className="w-12 h-12 rounded-full overflow-hidden shadow-sm border-2 border-white bg-gray-200">
          <img
            src={user?.picture || "https://i.pravatar.cc/150?img=default"}
            alt="User Profile"
            className="w-full h-full object-cover"
            referrerPolicy="no-referrer"
          />
        </div>
      </div>

      {/* Barra de Busca */}
      <div className="relative mb-8 shadow-sm rounded-2xl">
        <input
          type="text"
          placeholder="What are you looking for?"
          className="w-full bg-white rounded-2xl py-4 pl-12 pr-4 focus:ring-2 focus:ring-gray-200 outline-none text-sm border border-gray-100"
        />
        <span className="absolute left-4 top-4 text-gray-400 opacity-70">
          🔍
        </span>
      </div>

      <div className="flex justify-between items-center mb-5">
        <h2 className="text-lg font-bold text-gray-900">Available barbers</h2>
        <button className="text-gray-800 font-bold hover:bg-gray-200 rounded-full w-6 h-6 flex items-center justify-center transition">
          ⌄
        </button>
      </div>

      {/* 4. Renderizando a lista dinâmica */}
      <div className="space-y-4">
        {barbers.length === 0 ? (
          <p className="text-gray-500 text-center py-4">
            Nenhum barbeiro disponível no momento.
          </p>
        ) : (
          barbers.map((barber) => (
            <div
              key={barber.id}
              className="bg-white rounded-2xl p-4 flex items-center justify-between shadow-sm border border-gray-50 hover:shadow-md transition-shadow cursor-pointer"
            >
              <div className="flex items-center gap-4">
                <div className="relative">
                  <img
                    // Se o barbeiro não tiver foto no banco, usamos uma imagem padrão genérica
                    src={
                      barber.fotoUrl ||
                      `https://ui-avatars.com/api/?name=${barber.nome}&background=random`
                    }
                    alt={barber.nome}
                    className="w-14 h-14 rounded-full object-cover border-2 border-gray-50"
                  />
                  <div className="absolute bottom-0 right-0 w-3.5 h-3.5 bg-green-400 border-2 border-white rounded-full"></div>
                </div>

                <div>
                  <h3 className="font-bold text-gray-900">{barber.nome}</h3>
                  <div className="flex items-center text-xs text-gray-400 mt-1 font-medium">
                    <span className="text-yellow-400 mr-1 text-sm">★</span>
                    {/* Exibindo a nota média calculada pelo nosso DTO */}
                    <span>
                      {barber.notaMedia > 0
                        ? barber.notaMedia.toFixed(1)
                        : "Novo"}{" "}
                      • {barber.totalAvaliacoes} reviews
                    </span>
                  </div>
                </div>
              </div>

              <div className="flex flex-col items-end gap-2">
                <button className="bg-gray-900 text-white text-xs font-bold px-4 py-2.5 rounded-xl hover:bg-gray-700 transition-colors shadow-sm">
                  Book Now
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
