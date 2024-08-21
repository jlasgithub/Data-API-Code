#Backend Dockerfile
#Commands to Run: 
#docker build -t data-api-code .
#docker run -d -p 8080:8080 data-api-code

# Stage 1: Build the Spring Boot application
FROM gradle:7.6.0-jdk17 AS build

WORKDIR /app

# Copy the Gradle wrapper and configuration files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY ./gradlew ./
COPY ./gradlew.bat ./

# Copy the source code
COPY src ./src

# Run the Gradle build (skip tests for faster build)
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy the built jar file from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
