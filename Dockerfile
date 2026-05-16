# Multi-stage build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN useradd -r -u 1001 appuser
COPY --from=build /app/target/student-management.jar app.jar
EXPOSE 8080
USER appuser
ENTRYPOINT ["java","-jar","/app/app.jar"]

# To build the Docker image, run:
# docker build -t student-management:latest .