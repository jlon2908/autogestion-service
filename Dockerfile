# Utiliza una imagen oficial de OpenJDK como base
FROM openjdk:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo jar generado al contenedor
COPY target/autogestion-service-*.jar app.jar

# Expone el puerto en el que corre la aplicación (ajusta si es necesario)
EXPOSE 8085

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
