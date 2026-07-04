import { useState } from "react";
import { api } from "../services/api";
import { Link } from "react-router-dom";

/**
 * Tela de autenticação da aplicação.
 * Oferece suporte para login local via credenciais e login social (OAuth2).
 */
export function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  /**
   * Delega o fluxo de autenticação social para o backend (Spring Security).
   */
  const handleGoogleLogin = () => {
    window.location.href =
      "http://localhost:8080/api/oauth2/authorization/google";
  };

  /**
   * Autentica via JWT. Em caso de sucesso, força o recarregamento da página para
   * que a raiz da aplicação busque o perfil atualizado e defina a rota correta (Cliente vs Barbeiro).
   */
  const handleEmailLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      await api.post("/auth/login", { email, password });
      window.location.href = "/";
    } catch (err) {
      setError("Email ou senha incorretos.");
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
        <div className="mt-12">
          <h1 className="text-white text-5xl font-black tracking-tighter leading-none">
            THE
            <br />
            BARBER
          </h1>
          <p className="text-gray-300 mt-2 text-sm font-medium">
            Premium haircuts & beard grooming.
          </p>
        </div>

        <div className="mb-8 space-y-4">
          <form
            onSubmit={handleEmailLogin}
            className="space-y-3 bg-white/10 p-5 rounded-3xl backdrop-blur-sm border border-white/10"
          >
            {error && (
              <p className="text-red-400 text-xs font-bold text-center">
                {error}
              </p>
            )}
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
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full bg-white/5 border border-white/20 text-white rounded-xl px-4 py-3 text-sm focus:outline-none focus:ring-2 focus:ring-white/50 placeholder-gray-400"
              required
            />
            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-gray-100 text-gray-900 font-bold py-3 rounded-xl hover:bg-white transition-colors"
            >
              {isLoading ? "Signing in..." : "Sign In"}
            </button>
          </form>

          <div className="flex items-center gap-4 py-2">
            <div className="flex-1 h-px bg-white/20"></div>
            <span className="text-white/50 text-xs font-bold uppercase">
              or
            </span>
            <div className="flex-1 h-px bg-white/20"></div>
          </div>

          <button
            onClick={handleGoogleLogin}
            type="button"
            className="w-full bg-white text-gray-900 font-bold py-3 rounded-xl flex items-center justify-center gap-3 hover:bg-gray-100 transition-colors shadow-lg"
          >
            Continue with Google
          </button>

          <div className="text-center mt-6">
            <p className="text-gray-400 text-sm">
              Don't have an account?{" "}
              <Link
                to="/register"
                className="text-white font-bold hover:underline"
              >
                Sign Up
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
