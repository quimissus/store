# ---------------------
# 🌱 Stage 1: Build App
# ---------------------
FROM gradle:8.5-jdk17 AS build

# Create working directory inside container
WORKDIR /app

# Copy Gradle files first (to leverage Docker layer caching)
COPY build.gradle settings.gradle gradle.properties gradlew /app/
COPY gradle /app/gradle

# Download dependencies (improves rebuild time)
RUN ./gradlew build --no-daemon --stacktrace || true

# Copy the rest of the application
COPY . /app

# Build the Spring Boot application (skip tests in Docker build)
RUN ./gradlew bootJar --no-daemon

# -----------------------
# 🚀 Stage 2: Run App
# -----------------------
FROM eclipse-temurin:17-jre AS runtime

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port (optional for documentation)
EXPOSE 8085

# Set entrypoint to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
