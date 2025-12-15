# ETAPA 1: Compilación (Maven)
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila el código creando el JAR
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (Java)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copia el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]