FROM ubuntu:latest
LABEL authors="Marlon"

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/franquicia-api.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]