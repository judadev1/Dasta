FROM eclipse-temurin:21-jdk-jammy

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el JAR construido al contenedor
COPY target/*.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]