## Control2TBD ##

Trabajo de control 2 para Taller de Base de Datos.
Este proyecto se compone de 3 apartados:

1. Población de la Base de Datos
2. Backend
3. Frontend

## Requisitos Previos ##

- Java JDK (17) para el backend.
- Node.js (Para Vue) y npm para el frontend.
- PostgreSQL como motor de base de datos.
- Sistema operativo Windows.

## 1. Población de la Base de Datos ##

1. Crear una base de datos llamada control2TBD
2. Ejecutar todo el contenido del archivo Sql.txt ubicado en la raiz del proyecto.
3. Ejecutar todo el contenido del archivo PoblarSectoresComunas.sql ubicado en la raiz del proyecto.

## 2. Backend ##

Abrir terminal en la raiz del backend: Control2TBD\control2TBDBackEnd
Ejecuta estos comandos en la terminal antes de iniciar el backend:
set DATABASE_URL=jdbc:postgresql://localhost:5432/control2TBD
set DATABASE_USER=tu_usuario
set DATABASE_PASSWORD=tu_contraseña

[¡IMPORTANTE!]
Para ejecutar el backend, abre una terminal en esta carpeta y ejecuta el siguiente comando utilizando el Maven Wrapper:

.\mvnw spring-boot:run

## 3. Frontend ##

El código del frontend se encuentra en:

Control2TBD\control2TBDFrontEnd\control2TBDFrontend

Pasos para ejecutar el Frontend:
1. Abre una terminal en la carpeta del Frontend.
2. Instala las dependencias:

npm install

3. Ejecuta el servidor de desarrollo:

npm run dev

