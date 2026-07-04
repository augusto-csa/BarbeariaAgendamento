# BarbeariaAgendamento

Sistema web full stack para agendamento de horários em barbearias, com painéis separados para **clientes** e **barbeiros**, controle de disponibilidade em tempo real e login social via Google.

## Funcionalidades

- **Autenticação**: login local (e-mail/senha) e login social via Google (OAuth2), com sessão baseada em cookies.
- **Perfis de acesso (roles)**: `CLIENTE` e `BARBEIRO`, cada um com rotas e permissões próprias no frontend.
- **Cadastro de profissionais**: registro de barbeiros vinculando conta de usuário e perfil profissional.
- **Catálogo de serviços**: cadastro de serviços (nome, descrição, preço e duração).
- **Vínculo profissional × serviço**: definição de quais serviços cada barbeiro realiza.
- **Escala de trabalho**: configuração dos dias e horários de atendimento de cada barbeiro.
- **Cálculo de disponibilidade**: geração automática dos horários livres de um barbeiro em uma data, considerando escala de trabalho e agendamentos já existentes.
- **Agendamentos**: criação, listagem (por cliente ou por barbeiro) e cancelamento de horários.
- **Painéis dedicados**: dashboard do cliente (histórico e novos agendamentos) e dashboard do barbeiro (agenda e configuração de horários/serviços).
- **Documentação da API**: interface Swagger/OpenAPI gerada automaticamente.

## Tecnologias

**Backend**

- Java 21 + Spring Boot
- Spring Data JPA / Hibernate
- Spring Security + OAuth2 Client (login com Google)
- PostgreSQL
- Flyway (versionamento de banco de dados)
- springdoc-openapi (Swagger UI)
- Lombok
- Maven

**Frontend**

- React 19 + TypeScript
- Vite
- React Router DOM
- Tailwind CSS
- Axios

**Infraestrutura**

- Docker e Docker Compose

## Estrutura do projeto

```
BarbeariaAgendamento/
├── backend/               # API REST em Spring Boot
│   └── src/main/java/com/agendamento/barbearia/
│       ├── config/        # Segurança, OAuth2, Swagger
│       ├── core/          # Componentes centrais (UserDetailsService, etc.)
│       └── feature/       # Módulos por domínio (agendamento, usuario,
│                          # profissional, servico, horario, avaliacao...)
├── frontend/              # Aplicação React (SPA)
│   └── src/
│       ├── components/    # Telas (Login, Home, Booking, Dashboards...)
│       ├── services/      # Configuração do Axios e chamadas à API
│       └── routes.tsx     # Rotas protegidas por perfil de acesso
├── docker-compose.yml     # Orquestração de banco, backend e frontend
├── dev.sh                 # Script para subir o ambiente de desenvolvimento
└── .env.example           # Modelo de variáveis de ambiente
```

O backend segue uma organização por **feature**, onde cada domínio (`agendamento`, `usuario`, `profissional`, `servico`, `horario`, `profissional_servico`, `avaliacao`) possui suas próprias camadas de `model`, `dto`, `repo`, `service`, `mapper` e `controller`.

## Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose
- Para rodar fora do Docker (modo desenvolvimento local):
  - [Java 21](https://adoptium.net/)
  - [Node.js](https://nodejs.org/) (18+) e npm
  - [PostgreSQL](https://www.postgresql.org/)

## Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/augusto-csa/BarbeariaAgendamento.git
cd BarbeariaAgendamento
```

### 2. Configurar variáveis de ambiente

Copie o arquivo de exemplo e preencha com seus dados:

```bash
cp .env.example .env
```

```env
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha
POSTGRES_DB=barbearia_db
spring.security.oauth2.client.registration.google.client-id=SEU_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=SEU_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=profile,email
API_PORT=8080
```

> As credenciais OAuth2 do Google podem ser geradas no [Google Cloud Console](https://console.cloud.google.com/apis/credentials).

### 3a. Rodando com Docker

```bash
docker-compose up --build
```

Isso sobe três serviços:

- **db** — PostgreSQL na porta `5432`
- **backend** — API Spring Boot na porta definida em `API_PORT` (padrão `8080`)
- **frontend** — aplicação React na porta `5173`

### 3b. Rodando em modo desenvolvimento (script `dev.sh`)

Sobe apenas o banco via Docker e roda backend/frontend localmente:

```bash
chmod +x dev.sh
./dev.sh
```

### 4. Acessar a aplicação

| Serviço    | URL                                             |
| ---------- | ----------------------------------------------- |
| Frontend   | http://localhost:5173                           |
| API        | http://localhost:8080/api                       |
| Swagger UI | http://localhost:8080/api/swagger-ui/index.html |
