# Base image
FROM openjdk:17-jdk-slim AS base

WORKDIR /app

# Install Maven for building the project
RUN apt-get update && apt-get install -y maven

# Copy the Maven wrapper and POM files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Cache Maven dependencies
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Development stage
FROM base AS dev

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package

# Copy the JAR file to the /app directory
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev

CMD ["java", "-jar", "/app/user-service.jar"]

# Production stage
FROM openjdk:17-jdk-slim AS prod

WORKDIR /app

# Copy the JAR file from the development stage
COPY --from=dev /app/user-service.jar /app/user-service.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "/app/user-service.jar"]