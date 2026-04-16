# ESTÁGIO 1: Construção (Build)
# Usamos uma imagem que já tem o Maven e o Java 21 instalados
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos o arquivo de dependências e baixamos tudo (ajuda no cache do Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos o código-fonte e compilamos o projeto, gerando um arquivo .jar
COPY src ./src
RUN mvn clean package -DskipTests

# ESTÁGIO 2: Execução (Run)
# Agora usamos uma imagem muito mais leve, apenas com o ambiente de execução (JRE)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Pegamos o arquivo .jar que foi gerado no Estágio 1 e colocamos neste novo container
COPY --from=build /app/target/*.jar app.jar

# Avisamos que este container vai se comunicar pela porta 8080
EXPOSE 8080

# O comando que o container vai rodar quando ligar
ENTRYPOINT ["java", "-jar", "app.jar"]