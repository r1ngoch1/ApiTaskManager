FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

# Запускаем приложение
CMD ["java", "-jar", "app.jar"]
