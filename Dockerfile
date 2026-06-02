# Multi-stage Dockerfile to build and run the Spring Boot backend
# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy pom and download dependencies first (caching layer)
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw mvnw
RUN mvn -B -f pom.xml dependency:go-offline

# Copy sources and build
COPY src ./src
COPY pom.xml ./
RUN mvn -B -f pom.xml package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
