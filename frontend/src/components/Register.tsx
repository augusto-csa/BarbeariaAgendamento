import { useState } from "react";
import { api } from "../services/api";
import { Link, useNavigate } from "react-router-dom";

/**
 * Tela de criação de conta (Registro).
 * Permite o cadastro de novos usuários e a definição do perfil de acesso (Cliente ou Barbeiro).
 */
export function Register() {
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isBarber, setIsBarber] = useState(false);

  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      // A role do usuário é definida dinamicamente no payload de acordo com o checkbox
      await api.post("/usuarios", {
        nome,
        email,
        senha: password,
        role: isBarber ? "BARBEIRO" : "CLIENTE",
      });

      alert("Conta criada com sucesso! Faça login para continuar.");
      navigate("/login");
    } catch (err: any) {
      setError(
        err.response?.data?.message ||
          "Erro ao criar conta. O email pode já estar em uso.",
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="relative h-screen max-w-md mx-auto bg-gray-900 overflow-hidden font-sans">
      <img
        src="https://images.unsplash.com/photo-1585747860715-2ba37e788b70?q=80&w=1000&auto=format&fit=crop"
        alt="Barbershop Background"
        className="absolute inset-0 w-full h-full object-cover opacity-30"
      />
      <div className="relative z-10 h-full flex flex-col justify-between p-8">
        <div className="mt-8">
          <h1 className="text-white text-4xl font-black tracking-tighter leading-none">
            Junte-se
            <br />a nós
          </h1>
          <p className="text-gray-300 mt-2 text-sm font-medium">
            Crie a sua conta num instante.
          </p>
        </div>

        <div className="mb-8 space-y-4">
          <form
            onSubmit={handleRegister}
            className="space-y-3 bg-white/10 p-5 rounded-3xl backdrop-blur-sm border border-white/10"
          >
            {error && (
              <p className="text-red-400 text-xs font-bold text-center bg-red-900/20 py-2 rounded-lg">
                {error}
              </p>
            )}

            <input
              type="text"
              placeholder="Nome Completo"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              className="w-full bg-white/5 border border-white/20 text-white rounded-xl px-4 py-3 text-sm focus:outline-none focus:ring-2 focus:ring-white/50 placeholder-gray-400"
              required
            />
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full bg-white/5 border border-white/20 text-white rounded-xl px-4 py-3 text-sm focus:outline-none focus:ring-2 focus:ring-white/50 placeholder-gray-400"
              required
            />
            <input
              type="password"
              placeholder="Senha"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full bg-white/5 border border-white/20 text-white rounded-xl px-4 py-3 text-sm focus:outline-none focus:ring-2 focus:ring-white/50 placeholder-gray-400"
              required
            />

            {/* SELEÇÃO DE PERFIL PROFISSIONAL */}
            <label className="flex items-center gap-3 py-2 cursor-pointer group">
              <div
                className={`w-5 h-5 rounded-md border flex items-center justify-center transition-colors ${isBarber ? "bg-white border-white" : "border-white/40 group-hover:border-white/70"}`}
              >
                {isBarber && (
                  <span className="text-gray-900 text-xs font-black">✓</span>
                )}
              </div>
              <span className="text-gray-300 text-sm font-medium group-hover:text-white transition-colors">
                Sou um profissional (Barbeiro)
              </span>
              <input
                type="checkbox"
                className="hidden"
                checked={isBarber}
                onChange={() => setIsBarber(!isBarber)}
              />
            </label>

            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-white text-gray-900 font-bold py-3 rounded-xl hover:bg-gray-200 transition-colors mt-2"
            >
              {isLoading ? "A Criar Conta..." : "Criar Conta"}
            </button>
          </form>

          <div className="text-center mt-6">
            <p className="text-gray-400 text-sm">
              Já tens conta?{" "}
              <Link
                to="/login"
                className="text-white font-bold hover:underline"
              >
                Fazer Login
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
