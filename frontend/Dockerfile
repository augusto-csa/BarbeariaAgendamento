# Usamos uma imagem leve do Node.js
FROM node:20-alpine
WORKDIR /app

# Copiamos os arquivos de pacotes primeiro
COPY package*.json ./

# Instalamos as dependências do React
RUN npm install

# Copiamos o resto do código do frontend
COPY . .

# A porta padrão do Vite é a 5173
EXPOSE 5173

# O comando para iniciar o servidor. 
# O "--host" é crucial no Vite dentro do Docker para que ele aceite conexões de fora do container!
CMD ["npm", "run", "dev", "--", "--host"]