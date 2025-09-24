# Ultra-optimized multi-stage build
# Stage 1: Build the application
FROM openjdk:17-jdk-slim AS builder

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Ultra-optimized runtime with distroless
FROM gcr.io/distroless/java17-debian11 AS runtime

# Copy the built JAR from builder stage
COPY --from=builder /app/target/microservice-test-0.0.1-SNAPSHOT.jar /app.jar

# Expose port 8080
EXPOSE 8080

# Run the application with optimized JVM flags
CMD ["-jar", "/app.jar"]
