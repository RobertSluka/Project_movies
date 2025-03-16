# # Use a multi-stage build to cache dependencies
# FROM eclipse-temurin:21-jdk-alpine AS build
# WORKDIR /app

# # Copy Maven Wrapper and project files first (to cache dependencies)
# COPY mvnw ./
# COPY .mvn .mvn
# COPY pom.xml ./

# # Download dependencies without building the project (cached by Docker)
# RUN ./mvnw dependency:go-offline -B

# # Copy the source code after dependencies are cached
# COPY src ./src

# # Ensure Maven Wrapper is executable
# RUN chmod +x mvnw

# # Build the application JAR
# RUN ./mvnw clean package -DskipTests

# # Use a lightweight JDK image to run the application
# FROM eclipse-temurin:21-jre-alpine
# WORKDIR /app
# COPY --from=build /app/target/*.jar app.jar
# EXPOSE 8080
# CMD ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/movies-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

# trigger docker
