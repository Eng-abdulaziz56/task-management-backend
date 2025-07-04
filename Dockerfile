# Development Dockerfile
FROM maven:3.8.4-openjdk-17

# Set working directory
WORKDIR /app

# Copy Maven files first (for better caching)
COPY pom.xml .

# Download dependencies (leverages Docker cache)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Expose port
EXPOSE 8080

# Use Maven to run the application with Spring Boot DevTools
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-Dspring.devtools.restart.enabled=true -Dspring.devtools.livereload.enabled=true'"]