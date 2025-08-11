# ---------------------
# 🌱 Stage 1: Build the Spring Boot App
# ---------------------
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Copy only what's needed to resolve dependencies first (for Docker layer cache)
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Run Gradle to download dependencies (uses cache)
RUN ./gradlew clean build --no-daemon || true

# Copy the rest of the source code
COPY . /app

# Build the Spring Boot fat JAR (bootJar)
RUN ./gradlew bootJar --no-daemon

# -----------------------
# 🚀 Stage 2: Run the App
# -----------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create log directory if needed
RUN mkdir -p /app/logs

# Copy only the built JAR from the builder stage
#COPY --from=builder /app/build/libs/*.jar store-app.jar
COPY --from=builder /app/build/libs/store-1.0.0-SNAPSHOT.jar store-app.jar
# Optional: show contents (for debugging purposes)
RUN echo "Final app contents:" && ls -lh /app

EXPOSE 8085

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "store-app.jar"]
