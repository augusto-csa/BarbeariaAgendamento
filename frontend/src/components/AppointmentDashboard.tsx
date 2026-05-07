import { useEffect, useState } from "react";
import {
  fetchAppointments,
  type AppointmentResponse,
} from "../services/AppointmentService";

export function AppointmentDashboard() {
  const [appointments, setAppointments] = useState<AppointmentResponse[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadAppointments();
  }, []);

  const loadAppointments = async () => {
    try {
      setIsLoading(true);
      const data = await fetchAppointments();
      setAppointments(data);
    } catch (err) {
      console.error("Failed to fetch appointments:", err);
      setError("Could not load the appointment list. Is the backend running?");
    } finally {
      setIsLoading(false);
    }
  };

  const formatDateTime = (isoString: string) => {
    const date = new Date(isoString);
    return date.toLocaleString();
  };

  if (isLoading) return <h2>Loading appointments...</h2>;
  if (error) return <h2 style={{ color: "red" }}>{error}</h2>;

  return (
    <div style={{ padding: "20px", fontFamily: "sans-serif" }}>
      <h1>Barbershop Dashboard</h1>

      {appointments.length === 0 ? (
        <p>No appointments found.</p>
      ) : (
        <div style={{ display: "grid", gap: "15px" }}>
          {appointments.map((appointment) => (
            <div
              key={appointment.id}
              style={{
                border: "1px solid #ccc",
                padding: "15px",
                borderRadius: "8px",
              }}
            >
              <h3>Client: {appointment.cliente.nome}</h3>
              <p>
                <strong>Barber:</strong> {appointment.profissional.usuario.nome}
              </p>
              <p>
                <strong>Date & Time:</strong>{" "}
                {formatDateTime(appointment.dataHora)}
              </p>
              <p>
                <strong>Status:</strong> {appointment.status}
              </p>

              <h4>Services:</h4>
              <ul>
                {appointment.servicos.map((service) => (
                  <li key={service.id}>
                    {service.nome} - $ {service.preco.toFixed(2)} (
                    {service.duracaoMinutos} min)
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
