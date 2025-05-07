# Etapa 1: Construcción del JAR con Maven
FROM maven:3.9-eclipse-temurin as build

# Crear directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar todo el proyecto al contenedor
COPY . .

# Ejecutar Maven para compilar sin pruebas
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera solo con el JAR y Java 21
FROM eclipse-temurin:21-jdk-jammy

# Crear directorio de trabajo
WORKDIR /app

# Ciar el JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
