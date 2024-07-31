# Base stage
FROM openjdk:17-jdk-slim AS base

WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Development stage
FROM base AS dev

COPY src ./src

RUN ./mvnw clean package

COPY target/product-service-0.0.1-SNAPSHOT.jar /app/product-service.jar
EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=dev

CMD ["java", "-jar", "/app/product-service.jar"]

# Production stage
FROM openjdk:17-jdk-slim AS prod

WORKDIR /app

COPY --from=dev /app/product-service.jar /app/product-service.jar
EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "/app/product-service.jar"]

LABEL authors="prakharkumar"