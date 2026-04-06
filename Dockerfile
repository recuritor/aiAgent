# Build stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

# Copy only the files needed for dependency download and build
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

# Use the wrapper to download dependencies and build the boot jar
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the application jar from the build stage
COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
