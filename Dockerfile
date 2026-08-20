FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/usuario_recap.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/usuario_recap.jar"]