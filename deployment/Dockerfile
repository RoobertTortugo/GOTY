# Utiliza una imagen base de Java adecuada
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado por Gradle al contenedor
COPY applications/app-service/build/libs/GOTY.jar app.jar

# Exponer el puerto en el que escuchará tu aplicación (ajusta según tu configuración)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]