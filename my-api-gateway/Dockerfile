FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копируем заранее собранный .jar
COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]