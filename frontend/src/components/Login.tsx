import { useState } from "react";
import { api } from "../services/api";
import { Link } from "react-router-dom";

export function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleGoogleLogin = () => {
    window.location.href =
      "http://localhost:8080/api/oauth2/authorization/google";
  };

  const handleEmailLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      // Chama o nosso novo endpoint de login
      await api.post("/auth/login", { email, password });

      // Se deu certo, recarregamos a página para o App.tsx puxar os dados do usuário
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
          {/* Formulário de Login Padrão */}
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

          {/* Divisor Visual */}
          <div className="flex items-center gap-4 py-2">
            <div className="flex-1 h-px bg-white/20"></div>
            <span className="text-white/50 text-xs font-bold uppercase">
              or
            </span>
            <div className="flex-1 h-px bg-white/20"></div>
          </div>

          {/* Botão do Google */}
          <button
            onClick={handleGoogleLogin}
            type="button"
            className="w-full bg-white text-gray-900 font-bold py-3 rounded-xl flex items-center justify-center gap-3 hover:bg-gray-100 transition-colors shadow-lg"
          >
            <svg
              className="w-5 h-5"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                fill="#4285F4"
              />
              <path
                d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                fill="#34A853"
              />
              <path
                d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                fill="#FBBC05"
              />
              <path
                d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                fill="#EA4335"
              />
            </svg>
            Continue with Google
          </button>
          {/* Link para a tela de Cadastro */}
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
