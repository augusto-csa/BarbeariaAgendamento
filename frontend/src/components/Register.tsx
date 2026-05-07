import { useState } from "react";
import { api } from "../services/api";
import { Link, useNavigate } from "react-router-dom";

export function Register() {
  const navigate = useNavigate();

  // Estados para o formulário
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [telefone, setTelefone] = useState("");
  const [senha, setSenha] = useState("");

  // Estados de feedback
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      // O payload deve bater com os nomes dos atributos do seu RequestDTO no Java
      const payload = {
        nome: nome,
        email: email,
        telefone: telefone,
        senha: senha,
        role: "CLIENTE", // Define como cliente por padrão
      };

      await api.post("/usuarios", payload);

      // Se deu certo, manda para o login!
      navigate("/login");
    } catch (err: any) {
      // Tenta pegar a mensagem de erro do backend, se houver
      const mensagemErro =
        err.response?.data?.message ||
        "Erro ao criar conta. Tente outro email.";
      setError(mensagemErro);
      setIsLoading(false);
    }
  };

  return (
    <div className="relative h-screen max-w-md mx-auto bg-gray-900 overflow-hidden font-sans">
      {/* Background idêntico ao do Login */}
      <img
        src="https://images.unsplash.com/photo-1585747860715-2ba37e788b70?q=80&w=1000&auto=format&fit=crop"
        alt="Barbershop Background"
        className="absolute inset-0 w-full h-full object-cover opacity-30"
      />

      <div className="relative z-10 h-full flex flex-col justify-center p-8">
        <div className="mb-8">
          <h1 className="text-white text-4xl font-black tracking-tighter leading-none">
            JOIN THE
            <br />
            CLUB
          </h1>
          <p className="text-gray-300 mt-2 text-sm font-medium">
            Create an account to book your next cut.
          </p>
        </div>

        <div className="space-y-4">
          <form
            onSubmit={handleRegister}
            className="space-y-3 bg-white/10 p-5 rounded-3xl backdrop-blur-sm border border-white/10"
          >
            {error && (
              <p className="text-red-400 text-xs font-bold text-center">
                {error}
              </p>
            )}

            <input
              type="text"
              placeholder="Full Name"
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
              type="tel"
              placeholder="Phone Number"
              value={telefone}
              onChange={(e) => setTelefone(e.target.value)}
              className="w-full bg-white/5 border border-white/20 text-white rounded-xl px-4 py-3 text-sm focus:outline-none focus:ring-2 focus:ring-white/50 placeholder-gray-400"
              required
            />

            <input
              type="password"
              placeholder="Password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              className="w-full bg-white/5 border border-white/20 text-white rounded-xl px-4 py-3 text-sm focus:outline-none focus:ring-2 focus:ring-white/50 placeholder-gray-400"
              required
              minLength={6}
            />

            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-gray-100 text-gray-900 font-bold py-3 mt-2 rounded-xl hover:bg-white transition-colors"
            >
              {isLoading ? "Creating account..." : "Sign Up"}
            </button>
          </form>

          {/* Link para voltar ao Login */}
          <div className="text-center mt-6">
            <p className="text-gray-400 text-sm">
              Already have an account?{" "}
              <Link
                to="/login"
                className="text-white font-bold hover:underline"
              >
                Sign In
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
