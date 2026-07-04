import { api } from "./api";
export interface AppointmentResponse {
  id: number;
  dataHora: string;
  status: string;
  cliente: {
    nome: string;
    email: string;
  };
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

export const fetchAppointments = async (): Promise<AppointmentResponse[]> => {
  const response = await api.get("/agendamentos");
  return response.data;
};
