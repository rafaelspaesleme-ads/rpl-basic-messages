###
# Etapa de build usando Maven Wrapper e Java 17
FROM openjdk:17 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o Maven Wrapper para a imagem
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copia o arquivo settings.xml com as credenciais do repositório privado
COPY settings.xml /root/.m2/settings.xml

# Define as variáveis de ambiente
ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

ARG MONGO_HOST
ENV MONGO_HOST=${MONGO_HOST}

ARG MONGO_PORT
ENV MONGO_PORT=${MONGO_PORT}

ARG MONGO_USER
ENV MONGO_USER=${MONGO_USER}

ARG MONGO_PASS
ENV MONGO_PASS=${MONGO_PASS}

ARG MONGO_DB
ENV MONGO_DB=${MONGO_DB}

ARG MONGO_AUTH_DB
ENV MONGO_AUTH_DB=${MONGO_AUTH_DB}

ARG TOKEN_PUBLIC
ENV TOKEN_PUBLIC=${TOKEN_PUBLIC}

# Copia o código-fonte da aplicação
COPY src ./src

# Executa o build com o Maven Wrapper
RUN ./mvnw clean package -DskipTests --settings /root/.m2/settings.xml

# Etapa de runtime usando Java 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o jar da fase de build
COPY --from=build /app/target/rpl-basic-messages-0.0.1.jar rpl-basic-messages-0.0.1.jar

# Expõe a porta da aplicação
EXPOSE 8585

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "rpl-basic-messages-0.0.1.jar"]
