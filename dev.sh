#!/bin/bash

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}====================================================${NC}"
echo -e "${BLUE}  Iniciando Ambiente da Barbearia com Verificação  ${NC}"
echo -e "${BLUE}====================================================${NC}\n"

# 0. Limpeza e Carregamento
echo -e "${YELLOW}>> Limpando containers antigos e carregando .env...${NC}"
docker-compose down -v > /dev/null 2>&1
export $(grep -v '^#' .env | xargs)

# 1. Banco de Dados
echo -e "${GREEN}>> [1/3] Iniciando PostgreSQL...${NC}"
docker-compose up -d db
sleep 3

# 2. Backend
echo -e "${GREEN}>> [2/3] Iniciando Backend...${NC}"
cd backend
# Criamos um arquivo temporário de log para checar erros
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev > backend_start.log 2>&1 &
BACKEND_PID=$!
cd ..

# 3. Frontend
echo -e "${GREEN}>> [3/3] Iniciando Frontend...${NC}"
cd frontend
npm run dev > frontend_start.log 2>&1 &
FRONTEND_PID=$!
cd ..

echo -e "\n${GREEN}TUDO RODANDO COM SUCESSO!${NC}"
echo -e "Frontend: http://localhost:5173"
echo -e "API:      http://localhost:8080/api/swagger-ui/index.html"

# Função de limpeza
cleanup() {
    echo -e "\n\n${YELLOW}Encerrando serviços locais...${NC}"
    kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
    # Remove os arquivos de log temporários ao sair
    rm -f backend/backend_start.log frontend/frontend_start.log
    exit 0
}

trap cleanup SIGINT
wait $BACKEND_PID $FRONTEND_PID